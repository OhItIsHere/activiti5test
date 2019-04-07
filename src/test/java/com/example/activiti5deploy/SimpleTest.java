package com.example.activiti5deploy;


import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SimpleTest {

	ClassPathXmlApplicationContext ioc = new ClassPathXmlApplicationContext("spring/applicationContext-*.xml");
	// ProcessEngine���ṩ�˻�ȡ����Activiti����ķ���(������������)
	ProcessEngine processEngine = (ProcessEngine) ioc.getBean("processEngine");

	// 2.�������̶���
	@Test
	public void test02() {
		// ��ȡ���̶���Ͳ��������ص�Service
		RepositoryService repositoryService = processEngine.getRepositoryService();
		// createDeployment()�����������,addClasspathResource("mp.bpmn")������Դ�ļ���һ��ֻ�ܼ���һ���ļ�,deploy()//��ɲ���
		Deployment deploy = repositoryService.createDeployment().addClasspathResource("MyProcess.bpmn").deploy();
		System.out.println("deploy=" + deploy);
	}

}
