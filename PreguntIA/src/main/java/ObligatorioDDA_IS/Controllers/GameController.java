package ObligatorioDDA_IS.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ObligatorioDDA_IS.Services.QuestionService;

@RestController
public class GameController {

    private final QuestionService questionService;

    @Autowired
    public GameController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/getQuestion")
    public String getQuestion(@RequestParam String category, @RequestParam String difficulty) {
        return questionService.fetchQuestion(category, difficulty);
    }
}