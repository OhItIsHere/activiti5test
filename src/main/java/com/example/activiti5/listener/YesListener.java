package com.example.activiti5.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.context.ApplicationContext;

import com.example.activiti5.bean.StaffTravelProcess;
import com.example.activiti5.common.ApplicationContextUtils;
import com.example.activiti5.service.StaffTravelProcessService;
import com.example.activiti5.service.StaffTravelService;

public class YesListener implements ExecutionListener{

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		Integer tra_id = (Integer) execution.getVariable("tra_id");
		ApplicationContext applicationContext = ApplicationContextUtils.applicationContext;
		StaffTravelProcessService staffTravelProcessService = applicationContext.getBean(StaffTravelProcessService.class);
		StaffTravelService staffTravelService = applicationContext.getBean(StaffTravelService.class);
		//�������̱�
		StaffTravelProcess staffTravelProcess = staffTravelProcessService.query(tra_id);
		staffTravelProcess.setPstep("ͬ��");
		staffTravelProcess.setState(1);	
		staffTravelProcessService.update(staffTravelProcess);
		//���³����
		staffTravelService.updateState(tra_id);
	}

}
