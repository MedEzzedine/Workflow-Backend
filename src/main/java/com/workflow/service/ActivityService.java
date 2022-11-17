package com.workflow.service;

import com.workflow.entity.Demanderecu_id;
import com.workflow.entity.Role;
import com.workflow.entity.User;
import com.workflow.entity.Demande;
import com.workflow.util.JwtUtil;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityService {

    @Autowired
    private ManagementService managementService;


    @Autowired
    private RuntimeService runtimeService;


    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private JwtUtil jwtUtil;


    public void startProcess(int iddemande, int level) {

        Map<String, Object> variables = new HashMap<String, Object>();

        if (level == 0) {

            variables.put("traitement1role", "A/R_conge1");
            variables.put("level", 1);

            variables.put("iddemande", iddemande);
        } else {
            variables.put("traitement2role", "A/R_conge2");
            variables.put("level", 2);

            variables.put("iddemande", iddemande);
        }
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess", variables);

        System.out.println("Process started. Number of currently running"
                + "process instances= " + runtimeService.createProcessInstanceQuery().count());


    }


    public Demanderecu_id getalltask(HttpServletRequest request) {

        Demanderecu_id demanderecu_id = new Demanderecu_id(new ArrayList<Integer>(), new ArrayList<Integer>());
        User u = jwtUtil.getuserFromRequest(request);
        Role role = u.getRoles().stream().findFirst().get();
        List<Task> tasks_recu = new ArrayList<Task>();
        List<Task> tasks_nonarrive = new ArrayList<Task>();
        if (role.getNiveau() == 1) {
            tasks_recu = taskService.createTaskQuery().taskCandidateGroup("A/R_conge1").list();


        } else if (role.getNiveau() == 2) {
            tasks_nonarrive = taskService.createTaskQuery().taskCandidateGroup("A/R_conge1").list();
            tasks_recu = taskService.createTaskQuery().taskCandidateGroup("A/R_conge2").list();
        }
        if (tasks_recu.size() > 0) {
            for (Task task : tasks_recu) {
                demanderecu_id.getDemande_enattente_id().add((Integer) runtimeService.getVariables(task.getExecutionId()).get("iddemande"));

            }
        }
        if (tasks_nonarrive.size() > 0) {
            for (Task task : tasks_nonarrive) {
                demanderecu_id.getDemande_nonarrive_id().add((Integer) runtimeService.getVariables(task.getExecutionId()).get("iddemande"));

            }
        }
        return demanderecu_id;

    }

    /**
     * public User traitement(HttpServletRequest request,int id ,boolean AcceptOrRefus) {
     * <p>
     * User u=jwtUtil.getuserFromRequest(request);
     * Role  role=u.getRoles().stream().findFirst().get();
     * <p>
     * List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(role.getNom()).list();
     * String taskid=null;
     * for (Task task : tasks) {
     * if((Integer) runtimeService.getVariables(task.getExecutionId()).get("iddemande")==id)
     * taskid=task.getId();
     * <p>
     * // System.out.println(runtimeService.getVariables(task.getExecutionId()).get("iddemande"));
     * }
     * Map<String, Object> variables = new HashMap<String, Object>();
     * <p>
     * <p>
     * if (AcceptOrRefus) {
     * <p>
     * if (role.getNiveau()==2) {variables.put("accept", true);}else{
     * <p>
     * variables.put("traitement1", true);
     * variables.put("traitement2role",role.getRolesup().getNom());
     * }
     * }else {
     * <p>
     * if (role.getNiveau()==2) variables.put("accept", false);
     * else{
     * variables.put("traitement1", false);}
     * }
     * <p>
     * taskService.complete(taskid,variables);
     * System.out.println(taskid);
     * return u;
     * }
     **/

    public User multitraitement(HttpServletRequest request, List<Demande> demandes, boolean AcceptOrRefus, boolean otherdemande) {
        Map<String, Object> variables = new HashMap<String, Object>();
        User u = jwtUtil.getuserFromRequest(request);
        Role role = u.getRoles().stream().findFirst().get();
        List<Task> tasks = new ArrayList<Task>();

        if (role.getNiveau() == 1 || otherdemande) {

            tasks = taskService.createTaskQuery().taskCandidateGroup("A/R_conge1").list();
        } else if (role.getNiveau() == 2) {

            tasks = taskService.createTaskQuery().taskCandidateGroup("A/R_conge2").list();
        }
        List<Task> correspondingTasks = new ArrayList<Task>();
        for (Task task : tasks) {
            for (Demande demande : demandes) {
                if ((int) runtimeService.getVariables(task.getExecutionId()).get("iddemande") == demande.getId())
                    correspondingTasks.add(task);
            }
        }
        if (AcceptOrRefus) {

            if (role.getNiveau() == 2) {

                if (otherdemande) {
                    variables.put("traitement1", true);
                    variables.put("traitement2role", "A/R_conge2");
                } else variables.put("accept", true);
            } else {
                variables.put("traitement1", true);
                variables.put("traitement2role", "A/R_conge2");
            }
        } else {
            if (role.getNiveau() == 2) {
                if (otherdemande)
                    variables.put("traitement1", false);
                else
                    variables.put("accept", false);
            } else
                variables.put("traitement1", false);

        }

        //System.out.println("aa" + taskid);

        for (Task task : correspondingTasks) {
            int demandeId = (int) runtimeService.getVariables(task.getExecutionId()).get("iddemande");
            variables.put("iddemande", demandeId);
            taskService.complete(task.getId(), variables);
            variables.remove("iddemande");
        }
        return u;
    }
}
