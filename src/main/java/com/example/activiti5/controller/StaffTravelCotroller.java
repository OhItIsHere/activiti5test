package com.example.activiti5.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.activiti5.bean.StaffTravel;
import com.example.activiti5.bean.StaffTravelProcess;
import com.example.activiti5.common.AJAXResult;
import com.example.activiti5.listener.NoListener;
import com.example.activiti5.listener.SummaryListener;
import com.example.activiti5.listener.YesListener;
import com.example.activiti5.service.StaffService;
import com.example.activiti5.service.StaffTravelProcessService;
import com.example.activiti5.service.StaffTravelService;

@Controller
@RequestMapping("/stafftravel")
public class StaffTravelCotroller {

	@Autowired
	private StaffTravelService staffTravelService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private StaffTravelProcessService staffTravelProcessService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;

	@RequestMapping("/index")
	public String staff() {
		return "stafftravel";
	}

	@ResponseBody
	@RequestMapping("/appeal")
	public Object appeal(StaffTravel staffTravel) {
		String name = staffTravel.getStaffName();
		AJAXResult ajaxResult = new AJAXResult();
		//��������ݺϷ�
		if (staffService.queryByName(name) == 1 && staffService.queryStateByName(name) == 0) {			
			//������������
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
					.processDefinitionKey("leave").singleResult(); 
			Map<String, Object> map = new HashMap<>();
			map.put("travel", staffTravel.getTravel());		
			map.put("yes", new YesListener());
			map.put("no", new NoListener());
			map.put("summary", new SummaryListener());
			ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), map);
			//��¼����״��
			StaffTravelProcess process = new StaffTravelProcess();
			process.setPiid(processInstance.getId());
			process.setState(0);
			process.setTra_id(staffTravel.getId());
			if (staffTravel.getTravel() <= 10) {			
				process.setPstep("�鳤����");
			}
			else {
				process.setPstep("��������+��������");
			}
			//��������
			staffTravel.setDate(new Date());
			ajaxResult.setSuccess(true);
			ajaxResult.setData("����ɹ�!");
			staffTravelService.save(staffTravel);
			staffService.updateState(1, name);
			process.setTra_id(staffTravel.getId());
			staffTravelProcessService.save(process);
			Task task = taskService.createTaskQuery().processInstanceId(process.getPiid()).taskAssignee("����").
					singleResult();
			taskService.complete(task.getId());
			return ajaxResult;
		} else {
			ajaxResult.setSuccess(false);
			ajaxResult.setData("����ʧ��!");
			return ajaxResult;
		}
	}
}
