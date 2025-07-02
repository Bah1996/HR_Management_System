package hr_management_system.controller;


import hr_management_system.payload.ApiResponse;
import hr_management_system.payload.TaskDto;
import hr_management_system.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    TaskService taskService;

    // DIRECTOR OR MANAGER SEND THE TASK
    @PostMapping("/attachTask")
    public HttpEntity<?> attachTask(@RequestBody TaskDto taskDto){
        ApiResponse apiResponse = taskService.attachTask(taskDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    //EMPLOYEE DOING THE TASK
    @GetMapping("/doTheTask/{taskId}")
    public HttpEntity<?> doTheTask(@RequestParam String email, @RequestParam Integer taskId){
        ApiResponse apiResponse = taskService.doTheTask(taskId, email);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200:409).body(apiResponse);
    }

    //COMPLETING TASK
    @PostMapping("/completeTheTask/{id}")
    public HttpEntity<?> completeTask(@RequestParam Integer id){
        ApiResponse apiResponse = taskService.taskCompleted(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200:409).body(apiResponse);
    }
}
