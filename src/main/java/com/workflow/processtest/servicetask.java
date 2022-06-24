package com.workflow.processtest;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class servicetask  implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) {
		int a=(int) execution.getVariable(null);

}
	
}
