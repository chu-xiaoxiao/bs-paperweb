package com.zyc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zyc.exception.StatusException;
import com.zyc.model.Example.*;
import com.zyc.model.*;
import com.zyc.service.*;
import com.zyc.util.JSONResult;
import com.zyc.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YuChen Zhang on 18/03/12.
 */
@RestController
public class PaperController {
    @Autowired
    PaperService paperService;

    @Autowired
    PaperConfigService paperConfigService;

    @Autowired
    QuestionitopaperService questionitopaperService;

    @Autowired
    QuestionService questionService;

    @Autowired
    UserToPaperHistoryService userToPaperHistoryService;

    @RequestMapping(value = "createPaper",method = RequestMethod.POST)
    public Paper create(Paperconfig paperconfig, Integer subjectid) throws StatusException {
        Map<String,Object> paperconfigMap = new HashMap<>();
        paperconfigMap.put("paperconfig",paperconfig);
        paperconfigMap.put("subject",subjectid);
        Paper result = new JSONResult(paperService.createPaper(paperconfigMap)).getResult(Paper.class);
        paperconfig.setSubjectid(subjectid);
        paperconfig.setPaperconfigid(result.getPaperid());
        paperConfigService.save(paperconfig);
        paperconfig.setPaperconfigid(result.getPaperid());
        return result;
    }

    @RequestMapping(value = "queryPaper",method = RequestMethod.POST)
    public Page query(PaperExample paperExample, Integer current, Integer size) throws StatusException {
        Page<Paper,PaperExample> page = new Page<>(paperExample,current,size);
        page = new JSONResult(paperService.queryPaper(page)).getResult(Page.class);
        return page;
    }

    @RequestMapping(value = "query/{id}",method = RequestMethod.POST)
    public Paper query(@PathVariable("id") Integer id) throws StatusException {
        ModelAndView modelAndView = new ModelAndView();
        QuestionitopaperExample questionitopaperExample = new QuestionitopaperExample();
        questionitopaperExample.createCriteria().andPaperidEqualTo(id);
        List<Questionitopaper> questionitopapers = new JSONResult(questionitopaperService.query(questionitopaperExample)).getResultAsArray(Questionitopaper.class);
        List<Integer> questionIds = new ArrayList<>();
        for(Questionitopaper temp : questionitopapers){
            questionIds.add(temp.getQuestionid());
        }
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andQuestionidIn(questionIds);
        List<Question> questions = new JSONResult(questionService.queryQuestion(questionExample)).getResultAsArray(Question.class);
        Paper paper = new JSONResult(paperService.queryPaper(id)).getResult(Paper.class);

        List<Integer> scqid = new ArrayList<>();
        List<Integer> tfqid = new ArrayList<>();
        List<Integer> mcqid = new ArrayList<>();
        List<Integer> pqid  = new ArrayList<>();
        for(Question question : questions){
            if(question.getQuestiontype().equals(QuestionType.SCQ.getIndex())){
                scqid.add(question.getQuestionid());
            }
            if(question.getQuestiontype().equals(QuestionType.MCQ.getIndex())){
                mcqid.add(question.getQuestionid());
            }
            if(question.getQuestiontype().equals((QuestionType.TFQ).getIndex())){
                tfqid.add(question.getQuestionid());
            }
            if(question.getQuestiontype().equals(QuestionType.PQ.getIndex())){
                pqid.add(question.getQuestionid());
            }
        }
        if(scqid.size()!=0) {
            ScqExample scqExample = new ScqExample();
            scqExample.or().andScqidIn(scqid);
            paper.setScqs(new ArrayList<>());
            paper.getScqs().addAll(new JSONResult(questionService.queryQuestion(scqExample)).getResultAsArray(Scq.class));
        }
        if(mcqid.size()!=0) {
            McqExample mcqExample = new McqExample();
            mcqExample.or().andMcqidIn(mcqid);
            paper.setMcqs(new ArrayList<>());
            paper.getMcqs().addAll(new JSONResult(questionService.queryQuestion(mcqExample)).getResultAsArray(Mcq.class));
        }
        if(tfqid.size()!=0) {
            TfqExample tfqExample = new TfqExample();
            tfqExample.or().andTfqidIn(tfqid);
            paper.setTfqs(new ArrayList<>());
            paper.getTfqs().addAll(new JSONResult(questionService.queryQuestion(tfqExample)).getResultAsArray(Tfq.class));
        }
        if(pqid.size()!=0) {
            PqExample pqExample = new PqExample();
            pqExample.or().andPqidIn(pqid);
            paper.setPqs(new ArrayList<>());
            paper.getPqs().addAll(new JSONResult(questionService.queryQuestion(pqExample)).getResultAsArray(Pq.class));
        }
        return paper;
    }

    @RequestMapping("paperlist")
    public String query(User user,Integer currentpage,Integer size) throws Exception {
        //获取考生已经作答过得试卷
        Usertopaperhistory usertopaperhistory = new Usertopaperhistory();
        UsertopaperhistoryExample usertopaperhistoryExample = new UsertopaperhistoryExample();
        usertopaperhistoryExample.createCriteria().andUseridEqualTo(21);
        List<Usertopaperhistory> old = new JSONResult(userToPaperHistoryService.query(usertopaperhistoryExample)).getResultAsArray(Usertopaperhistory.class);

        PaperExample paperExample = new PaperExample();
        paperExample.createCriteria().andPapertypeEqualTo(1);
        Page<Paper,PaperExample> page = new Page<>(paperExample,currentpage,size);
        page = new JSONResult(paperService.queryPaper(page)).getResult(Page.class);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("page",page);
        List<Double> score = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        //获取以作答试卷id
        for(int i=0;i<page.getList().size();i++){
            Map<String,Object> temp = (Map<String, Object>) page.getList().get(i);
            ids.add(Integer.parseInt(temp.get("paperid").toString()));
        }
        //组织得分
        for(Usertopaperhistory temp : old){
            if(ids.contains(temp.getPaperid())){
                score.add(temp.getValue());
            }else{
                score.add(null);
            }
        }
        jsonObject.put("scores",score);
        return jsonObject.toString();
    }
}
