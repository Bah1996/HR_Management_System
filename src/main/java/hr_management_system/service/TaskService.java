package hr_management_system.service;

import hr_management_system.entity.Role;
import hr_management_system.entity.Task;
import hr_management_system.entity.User;
import hr_management_system.entity.enums.RoleName;
import hr_management_system.entity.enums.TaskStatus;
import hr_management_system.payload.ApiResponse;
import hr_management_system.payload.TaskDto;
import hr_management_system.repository.RoleRepository;
import hr_management_system.repository.TaskRepository;
import hr_management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    RoleRepository roleRepository;

    //SEND THE MAIL TO EMPLOYEE AND MANAGERS
    public void sendTaskToEmployee(Integer tasId, String email){
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("example@gmail.com");
            mailMessage.setTo(email);
            mailMessage.setSubject("Follow the link to seeing new Task for you! " +
                    "\nhttp://localhost:8080/api/doTheTask?email=" + email + "&taskId" + tasId);
            javaMailSender.send(mailMessage);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //ACTIVATED TASK WHEN CLICKED THE LINK
    public ApiResponse doTheTask(Integer taskId, String email){
        Optional<Task> optionalTask = taskRepository.findByIdAndToUser_Email(taskId, email);
        if (optionalTask.isEmpty())
            return new ApiResponse("Task not found", false);

        Task currentTask = optionalTask.get();
        currentTask.setTaskStatus(TaskStatus.IN_PROGRESS);

        taskRepository.save(currentTask);

        return new ApiResponse("Task is now Active", true);
    }

    //ATTACHING THE TASK TO THE EMPLOYEE
    public ApiResponse attachTask(TaskDto taskDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //USER
        Optional<User> optionalUser = userRepository.findById(taskDto.getEmployeeId());
        if (optionalUser.isEmpty())
            return new ApiResponse("Employe not found", false);

        if (authentication != null && !authentication.getPrincipal().equals("anonymousUser")){
            User userContext = (User) authentication.getPrincipal();
            Set<Role> roles = userContext.getRoles();

            for (Role role : roles){
                int roleId = 0;

                switch (role.getRoleName().name()) {
                    case "DIRECTOR" -> roleId = roleRepository.findByRoleName(RoleName.DIRECTOR).getId();
                    case "MANAGER", "HR_MANAGER" -> roleId = 2;
                    default -> {
                        return new ApiResponse("Role not found", false);
                    }
                }

                Set<Role> toUserRole = optionalUser.get().getRoles();
                int roleId1 = 0;
                for (Role responsibleRole : toUserRole){
                    switch (responsibleRole.getRoleName().name()) {
                        case "DIRECTOR" -> roleId1 = roleRepository.findByRoleName(RoleName.DIRECTOR).getId();
                        case "HR_MANAGER", "MANAGER" -> roleId1 = 2;
                        case "EMPLOYEE" -> roleId1 = roleRepository.findByRoleName(RoleName.EMPLOYEE).getId();
                        default -> {
                            return new ApiResponse("Role not found", false);
                        }
                    }
                }

                boolean taskStatus = false;
                if (roleId == roleRepository.findByRoleName(RoleName.DIRECTOR).getId()
                        && roleId1 != roleRepository.findByRoleName(RoleName.DIRECTOR).getId()){
                    taskStatus = true;
                }

                if (roleId == 2 && roleId1 != roleRepository.findByRoleName(RoleName.DIRECTOR).getId()){
                    taskStatus = true;
                }

                if (!taskStatus){
                    return new ApiResponse("You cannot assign task to this Employee", false);
                }
            }

            Task task = new Task();
            task.setTaskName(taskDto.getTaskName());
            task.setDescription(taskDto.getDescription());
            task.setDeadline(taskDto.getDeadline());
            task.setTaskStatus(TaskStatus.NEW);
            task.setFromUser(userContext);
            task.setToUser(optionalUser.get());
            taskRepository.save(task);

            //SEND EMAIL
            sendTaskToEmployee(task.getId(), optionalUser.get().getEmail());
        }
        return new ApiResponse("Task has been successfully sent", true);
    }

    //SENDING EMAIL ABOUT THE COMPLETED TASK OF EMPLOYEES TO THE MANAGER OR DIRECTOR
    public void sendEmailToManager(String email, UUID id){
        try {
            Optional<User> optionalUser = userRepository.findById(id);
            if (optionalUser.isPresent()){
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("example@gmail.com");
                message.setTo(email);
                message.setSubject("Task Completed");
                message.setText("The Employee " + optionalUser.get().getFirstName() + " " + optionalUser.get().getLastName()
                                    + " has finished theTask");
                javaMailSender.send(message);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //COMPLETING TASK
    public ApiResponse taskCompleted(Integer id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userContext = (User) authentication.getPrincipal();

        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isEmpty())
            return new ApiResponse("Task not found", false);

        Task task = optionalTask.get();
        if (task.getToUser().getUsername().equals(userContext.getUsername())){

            if (task.getTaskStatus().equals(TaskStatus.COMPLETED)){
                return new ApiResponse("Task has been already completed", false);
            }

            task.setTaskStatus(TaskStatus.COMPLETED);
            taskRepository.save(task);

            sendEmailToManager(optionalTask.get().getFromUser().getEmail(), optionalTask.get().getToUser().getId());

            return new ApiResponse("Tas kcompleted", true);
        }
        return new ApiResponse("It is not your Task", false);
    }
}