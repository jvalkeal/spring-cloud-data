/*
 * Copyright 2020-2021 the original author or authors.
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

package org.springframework.cloud.dataflow.integration.test;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Christian Tzolov
 */
@ConfigurationProperties(prefix = "test")
public class IntegrationTestProperties {

	private DatabaseProperties database = new DatabaseProperties();
	private PlatformProperties platform = new PlatformProperties();

	public DatabaseProperties getDatabase() {
		return database;
	}

	public void setDatabase(DatabaseProperties database) {
		this.database = database;
	}

	public PlatformProperties getPlatform() {
		return platform;
	}

	public void setPlatform(PlatformProperties platform) {
		this.platform = platform;
	}

	public static class DatabaseProperties {

		private String dataflowVersion;
		private String skipperVersion;
		private boolean sharedDatabase;

		public String getDataflowVersion() {
			return dataflowVersion;
		}

		public void setDataflowVersion(String dataflowVersion) {
			this.dataflowVersion = dataflowVersion;
		}

		public String getSkipperVersion() {
			return skipperVersion;
		}

		public void setSkipperVersion(String skipperVersion) {
			this.skipperVersion = skipperVersion;
		}

		public boolean isSharedDatabase() {
			return sharedDatabase;
		}

		public void setSharedDatabase(boolean sharedDatabase) {
			this.sharedDatabase = sharedDatabase;
		}
	}

	public static class PlatformProperties {

		private PlatformConnectionProperties connection = new PlatformConnectionProperties();

		public PlatformConnectionProperties getConnection() {
			return connection;
		}

		public void setConnection(PlatformConnectionProperties connection) {
			this.connection = connection;
		}
	}

	public static class PlatformConnectionProperties {

		/**
		 * Default url to connect to dataflow
		 */
		private String dataflowServerUrl = "http://localhost:9393";

		/**
		 * default - local platform, cf - Cloud Foundry platform, k8s - GKE/Kubernetes platform
		 */
		private String platformName = "default";

		/**
		 * Default url to connect to SCDF's Prometheus TSDB
		 */
		private String prometheusUrl = "http://localhost:9090";

		/**
		 * Default url to connect to SCDF's Influx TSDB
		 */
		private String influxUrl = "http://localhost:8086";

		/**
		 * Kubernetes tests require nginx-ingress and watchdog mechanism to expose the
		 * apps URL to public access. Later requires a preconfigured ingress servers
		 * domain name suffix.
		 *
		 * For example the Hydra AT environment hash the 'hydra.springapps.io' host name
		 * and app Urls will have the form
		 * https://partitioning-test-log-v1.hydra.springapps.io or
		 * https://partitioning-test-log-v1-1.hydra.springapps.io for multiple instance.
		 */
		private String kubernetesAppHostSuffix = "";

		public String getDataflowServerUrl() {
			return dataflowServerUrl;
		}

		public void setDataflowServerUrl(String dataflowServerUrl) {
			this.dataflowServerUrl = dataflowServerUrl;
		}

		public String getPlatformName() {
			return platformName;
		}

		public void setPlatformName(String platformName) {
			this.platformName = platformName;
		}

		public String getPrometheusUrl() {
			return prometheusUrl;
		}

		public void setPrometheusUrl(String prometheusUrl) {
			this.prometheusUrl = prometheusUrl;
		}

		public String getInfluxUrl() {
			return influxUrl;
		}

		public void setInfluxUrl(String influxUrl) {
			this.influxUrl = influxUrl;
		}

		public String getKubernetesAppHostSuffix() {
			return kubernetesAppHostSuffix;
		}

		public void setKubernetesAppHostSuffix(String kubernetesAppHostSuffix) {
			this.kubernetesAppHostSuffix = kubernetesAppHostSuffix;
		}
	}
}
