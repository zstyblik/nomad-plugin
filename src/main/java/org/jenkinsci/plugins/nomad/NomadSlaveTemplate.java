package org.jenkinsci.plugins.nomad;

import hudson.Extension;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import hudson.Util;
import hudson.util.FormValidation;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Label;
import hudson.model.Node;
import hudson.model.labels.LabelAtom;
import jenkins.model.Jenkins;

import java.lang.reflect.Type;
import com.google.common.base.Strings;
import com.google.gson.reflect.TypeToken;

import javax.annotation.Nullable;
import java.util.*;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class NomadSlaveTemplate implements Describable<NomadSlaveTemplate> {

    private static final String SLAVE_PREFIX = "jenkins-";
    private static final Logger LOGGER = Logger.getLogger(NomadSlaveTemplate.class.getName());

    private final int idleTerminationInMinutes;
    private final Boolean reusable;
    private final int numExecutors;

    private final int cpu;
    private final int memory;
    private final int disk;
    private final int priority;
    private final String labels;
    private final List<? extends NomadConstraintTemplate> constraints;
    private final String region;
    private final String remoteFs;
    private final String image;
    private final Boolean privileged;
    private final String network;
    private final String username;
    private final String password;
    private final String prefixCmd;
    private final Boolean forcePull;
    private final String hostVolumes;
    private final String switchUser;
    private final Node.Mode mode;

    private NomadCloud cloud;
    private String driver;
    private String datacenters;
    private Set<LabelAtom> labelSet;

    public static final String DEFAULT_DATACENTERS = "dc1";
    public static final int DEFAULT_CPU = 256;
    public static final int DEFAULT_DISK = 300;
    public static final int DEFAULT_IDLE_TERMINATION_IN_MINUTES = 10;
    public static final int DEFAULT_MEMORY = 1024;
    public static final int DEFAULT_NUM_EXECUTORS = 1;
    public static final int DEFAULT_PRIORITY = 1;

    @DataBoundConstructor
    public NomadSlaveTemplate(
            String cpu,
            String memory,
            String disk,
            String labels,
            List<? extends NomadConstraintTemplate> constraints,
            String remoteFs,
            String idleTerminationInMinutes,
            Boolean reusable,
            String numExecutors,
            Node.Mode mode,
            String region,
            String priority,
            String image,
            String datacenters,
            String username,
            String password,
            Boolean privileged,
            String network,
            String prefixCmd,
            Boolean forcePull,
            String hostVolumes,
            String switchUser
            ) {
        if (!Strings.isNullOrEmpty(cpu)) {
            int cpuConverted = Integer.parseInt(cpu);
            if (cpuConverted < 1) {
                this.cpu = DEFAULT_CPU;
            } else {
                this.cpu = cpuConverted;
            }
        } else {
            this.cpu = DEFAULT_CPU;
        }

        if (!Strings.isNullOrEmpty(memory)) {
            int memoryConverted = Integer.parseInt(memory);
            if (memoryConverted < 1) {
                this.memory = DEFAULT_MEMORY;
            } else {
                this.memory = memoryConverted;
            }
        } else {
            this.memory = DEFAULT_MEMORY;
        }

        if (!Strings.isNullOrEmpty(disk)) {
            int diskConverted = Integer.parseInt(disk);
            if (diskConverted < 1) {
                this.disk = DEFAULT_DISK;
            } else {
                this.disk = diskConverted;
            }
        } else {
            this.disk = DEFAULT_DISK;
        }

        if (!Strings.isNullOrEmpty(priority)) {
            int priorityConverted = Integer.parseInt(priority);
            if (priorityConverted < 1) {
                this.priority = DEFAULT_PRIORITY;
            } else {
                this.priority = priorityConverted;
            }
        } else {
            this.priority = DEFAULT_PRIORITY;
        }
        if (!Strings.isNullOrEmpty(idleTerminationInMinutes)) {
            int idleTerminationInMinutesConverted = Integer.parseInt(idleTerminationInMinutes);
            if (idleTerminationInMinutesConverted < 0) {
                this.idleTerminationInMinutes = DEFAULT_IDLE_TERMINATION_IN_MINUTES;
            } else {
                this.idleTerminationInMinutes = idleTerminationInMinutesConverted;
            }
        } else {
            this.idleTerminationInMinutes = DEFAULT_IDLE_TERMINATION_IN_MINUTES;
        }

        this.reusable = reusable;

        if (!Strings.isNullOrEmpty(numExecutors)) {
            int numExecutorsConverted = Integer.parseInt(numExecutors);
            if (numExecutorsConverted < 1) {
                this.numExecutors = DEFAULT_NUM_EXECUTORS;
            } else {
                this.numExecutors = numExecutorsConverted;
            }
        } else {
            this.numExecutors = DEFAULT_NUM_EXECUTORS;
        }

        this.mode = mode;
        this.remoteFs = remoteFs;
        this.labels = Util.fixNull(labels);
        if (constraints == null) {
            this.constraints = Collections.emptyList();
        } else {
            this.constraints = constraints;
        }
        this.labelSet = Label.parse(labels);
        this.region = region;
        this.image = image;

        if (!Strings.isNullOrEmpty(datacenters)) {
            this.datacenters = datacenters;
        } else {
            this.datacenters = DEFAULT_DATACENTERS;
        }

        this.username = username;
        this.password = password;
        this.privileged = privileged;
        this.network = network;
        this.prefixCmd = prefixCmd;
        this.forcePull = forcePull;
        this.hostVolumes = hostVolumes;
        this.switchUser = switchUser;
        readResolve();
    }

    protected Object readResolve() {
        this.driver = !this.image.equals("") ? "docker" : "java";
        return this;
    }

    @Extension
    public static final class DescriptorImpl extends Descriptor<NomadSlaveTemplate> {

        public DescriptorImpl() {
            load();
        }

        @Override
        public String getDisplayName() {
            return null;
        }

        public int getDefaultCPU() {
            return DEFAULT_CPU;
        }

        public String getDefaultDatacenters() {
            return DEFAULT_DATACENTERS;
        }

        public int getDefaultDisk() {
            return DEFAULT_DISK;
        }

        public int getDefaultMemory() {
            return DEFAULT_MEMORY;
        }

        public int getDefaultIdleTerminationInMinutes() {
            return DEFAULT_IDLE_TERMINATION_IN_MINUTES;
        }

        public int getDefaultNumExecutors() {
            return DEFAULT_NUM_EXECUTORS;
        }

        public int getDefaultPriority() {
            return DEFAULT_PRIORITY;
        }

        public FormValidation doCheckCpu(@QueryParameter String cpu) {
            if (!isInt(cpu)) {
                return FormValidation.error("CPU must be set and be a number");
            }

            int cpuConverted = Integer.parseInt(cpu);
            if (cpuConverted < 1) {
                return FormValidation.error("CPU must be greater than 0");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckDatacenters(@QueryParameter String datacenters) {
            if (Strings.isNullOrEmpty(datacenters)) {
                return FormValidation.error("Datacenters must be set");
            } else {
                return FormValidation.ok();
            }
        }

        public FormValidation doCheckDisk(@QueryParameter String disk) {
            if (!isInt(disk)) {
                return FormValidation.error("Disk must be set and be a number");
            }

            int diskConverted = Integer.parseInt(disk);
            if (diskConverted < 1) {
                return FormValidation.error("Disk must be greater than 0");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckIdleTerminationInMinutes(@QueryParameter String idleTerminationInMinutes) {
            if (!isInt(idleTerminationInMinutes)) {
                return FormValidation.error("Idle Termination Time must be set and be a number");
            }

            int itimConverted = Integer.parseInt(idleTerminationInMinutes);
            if (itimConverted < 0) {
                return FormValidation.error("Idle Termination Time must be greater than or equal to 0");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckMemory(@QueryParameter String memory) {
            if (!isInt(memory)) {
                return FormValidation.error("Memory must be set and be a number");
            }

            int memoryConverted = Integer.parseInt(memory);
            if (memoryConverted < 1) {
                return FormValidation.error("Memory must be greater than 0");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckNumExecutors(@QueryParameter String numExecutors) {
            if (!isInt(numExecutors)) {
                return FormValidation.error("Number of Executors must be set and be a number");
            }

            int numExecutorsConverted = Integer.parseInt(numExecutors);
            if (numExecutorsConverted < 1) {
                return FormValidation.error("Number of Executors must be greater than 0");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckPriority(@QueryParameter String priority) {
            if (!isInt(priority)) {
                return FormValidation.error("Priority must be set and be a number");
            }

            int priorityConverted = Integer.parseInt(priority);
            if (priorityConverted < 1) {
                return FormValidation.error("Priority must be greater than 0");
            }

            return FormValidation.ok();
        }
    }

    static Boolean isInt(String str) {
        if (!Strings.isNullOrEmpty(str) && str.matches("^-?\\d+$")) {
            return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Descriptor<NomadSlaveTemplate> getDescriptor() {
        return Jenkins.getInstance().getDescriptor(getClass());
    }

    public String createSlaveName() {
        return SLAVE_PREFIX + Long.toHexString(System.nanoTime());
    }

    public Set<LabelAtom> getLabelSet() {
        return labelSet;
    }

    public int getNumExecutors() {
        return numExecutors;
    }

    public Node.Mode getMode() {
        return mode;
    }

    public int getCpu() {
        return cpu;
    }

    public int getMemory() {
        return memory;
    }

    public String getLabels() {
        return labels;
    }

    public List<NomadConstraintTemplate> getConstraints() {
        return Collections.unmodifiableList(constraints);
    }

    public int getIdleTerminationInMinutes() {
        return idleTerminationInMinutes;
    }

    public Boolean getReusable() {
        return reusable;
    }

    public String getRegion() {
        return region;
    }

    public String getDatacenters() {
        return datacenters;
    }

    public int getPriority() {
        return priority;
    }

    public int getDisk() {
        return disk;
    }

    public void setCloud(NomadCloud cloud) {
        this.cloud = cloud;
    }

    public NomadCloud getCloud() {
        return cloud;
    }

    public String getRemoteFs() {
        return remoteFs;
    }

    public String getImage() {
        return image;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPrefixCmd() {
        return prefixCmd;
    }

    public String getDriver() {
        return driver;
    }

    public Boolean getPrivileged() {
        return privileged;
    }

    public String getNetwork() {
        return network;
    }

    public Boolean getForcePull() {
        return forcePull;
    }

    public String getHostVolumes() {
        return hostVolumes;
    }

    public String getSwitchUser() {
        return switchUser;
    }
}
