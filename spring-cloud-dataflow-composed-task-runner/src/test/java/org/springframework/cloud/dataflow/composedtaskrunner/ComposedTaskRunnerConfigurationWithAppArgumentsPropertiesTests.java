/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.dataflow.composedtaskrunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDataSourceConfiguration;
import org.springframework.cloud.common.security.CommonSecurityAutoConfiguration;
import org.springframework.cloud.dataflow.composedtaskrunner.configuration.DataFlowTestConfiguration;
import org.springframework.cloud.dataflow.composedtaskrunner.properties.ComposedTaskProperties;
import org.springframework.cloud.dataflow.rest.client.TaskOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

/**
 * @author Janne Valkealahti
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes={EmbeddedDataSourceConfiguration.class,
		DataFlowTestConfiguration.class,StepBeanDefinitionRegistrar.class,
		ComposedTaskRunnerConfiguration.class,
		StepBeanDefinitionRegistrar.class})
@TestPropertySource(properties = {"graph=ComposedTest-AAA && ComposedTest-BBB && ComposedTest-CCC","max-wait-time=1010",
// "composed-task-properties=" + ComposedTaskRunnerConfigurationWithAppArgumentsPropertiesTests.COMPOSED_TASK_PROPS ,
        "interval-time-between-checks=1100", "composed-task-arguments=--baz=boo --AAA.foo=bar BBB.que=qui",
        "composed-task-app-arguments.app.ComposedTest-AAA.AAA=--arg1=value1",
		"dataflow-server-uri=https://bar", "spring.cloud.task.name=ComposedTest"})
@EnableAutoConfiguration(exclude = { CommonSecurityAutoConfiguration.class})
public class ComposedTaskRunnerConfigurationWithAppArgumentsPropertiesTests {

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private Job job;

	@Autowired
	private TaskOperations taskOperations;

	@Autowired
	private ComposedTaskProperties composedTaskProperties;

	protected static final String COMPOSED_TASK_PROPS = "app.ComposedTest-AAA.AAA=--arg1=value1";

	@Test
	@DirtiesContext
	public void testComposedConfiguration() throws Exception {
		JobExecution jobExecution = this.jobRepository.createJobExecution(
				"ComposedTest", new JobParameters());
		job.execute(jobExecution);

		Map<String, String> props = new HashMap<>(1);
		// props.put("version.AAA", "1.0.0");
        // assertThat(composedTaskProperties.getComposedTaskProperties()).isEqualTo(COMPOSED_TASK_PROPS);
        // Map<String, String> xxx = composedTaskProperties.getComposedTaskAppArguments();
		assertThat(composedTaskProperties.getComposedTaskProperties()).isNull();
		assertThat(composedTaskProperties.getMaxWaitTime()).isEqualTo(1010);
		assertThat(composedTaskProperties.getIntervalTimeBetweenChecks()).isEqualTo(1100);
		assertThat(composedTaskProperties.getDataflowServerUri().toASCIIString()).isEqualTo("https://bar");
		List<String> args = new ArrayList<>(1);
		args.add("--baz=boo --foo=bar --arg1=value1");
		assertThat(job.getJobParametersIncrementer()).withFailMessage("JobParametersIncrementer must not be null.").isNotNull();
		verify(this.taskOperations).launch("ComposedTest-AAA", props, args);
	}
}
