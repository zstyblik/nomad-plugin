package org.jenkinsci.plugins.nomad;

import hudson.model.Node;
import org.junit.Test;

import java.util.Collections;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.util.List;
import java.util.ArrayList;

public class NomadSlaveTemplateTest {

    @Test
    public void testNomadSlaveTemplateInitWithValidInput() {
        List<NomadConstraintTemplate> constraintTest = new ArrayList<NomadConstraintTemplate>();
        NomadSlaveTemplate slaveTemplate = new NomadSlaveTemplate(
            "300", /* CPU */
            "256", /* Memory */
            "100", /* Disk */
            null, /* labels */
            constraintTest, /* constraints */
            "remoteFs", /* remoteFS */
            "3", /* idle timeout */
            true, /* reusable */
            "1", /* num executors */
            Node.Mode.NORMAL, /* mode */
            "ams", /* region */
            "1", /* priority */
            "image", /* image */
            "dc01", /* datacenter */
            "", /* username */
            "", /* password */
            false, /* privileged */
            "bridge", /* network */
            "", /* prefix cmd */
            true, /* force pull */
            "/mnt:/mnt", /* host volumes */
            "jenkins" /* switch user */
        );
        assertTrue(slaveTemplate.getCpu() == 300);
        assertTrue(slaveTemplate.getMemory() == 256);
        assertTrue(slaveTemplate.getDisk() == 100);
        assertTrue(slaveTemplate.getLabels() == "");
        assertTrue(slaveTemplate.getRemoteFs() == "remoteFs");
        assertTrue(slaveTemplate.getIdleTerminationInMinutes() == 3);
        assertTrue(slaveTemplate.getReusable() == true);
        assertTrue(slaveTemplate.getNumExecutors() == 1);
        assertTrue(slaveTemplate.getRegion() == "ams");
        assertTrue(slaveTemplate.getPriority() == 1);
        assertTrue(slaveTemplate.getImage() == "image");
        assertTrue(slaveTemplate.getDatacenters() == "dc01");
        assertTrue(slaveTemplate.getUsername() == "");
        assertTrue(slaveTemplate.getPassword() == "");
        assertTrue(slaveTemplate.getPrivileged() == false);
        assertTrue(slaveTemplate.getNetwork() == "bridge");
        assertTrue(slaveTemplate.getPrefixCmd() == "");
        assertTrue(slaveTemplate.getForcePull() == true);
        assertTrue(slaveTemplate.getHostVolumes() == "/mnt:/mnt");
        assertTrue(slaveTemplate.getSwitchUser() == "jenkins");
    }

    @Test
    public void testNomadSlaveTemplateInitWithInvalidInput() {
        List<NomadConstraintTemplate> constraintTest = new ArrayList<NomadConstraintTemplate>();
        NomadSlaveTemplate slaveTemplate = new NomadSlaveTemplate(
            "", /* CPU */
            "", /* Memory */
            "", /* Disk */
            null, /* labels */
            constraintTest, /* constraints */
            "remoteFs", /* remoteFS */
            "", /* idle timeout */
            true, /* reusable */
            "", /* num executors */
            Node.Mode.NORMAL, /* mode */
            "ams", /* region */
            "", /* priority */
            "image", /* image */
            "", /* datacenter */
            "", /* username */
            "", /* password */
            false, /* privileged */
            "bridge", /* network */
            "", /* prefix cmd */
            true, /* force pull */
            "/mnt:/mnt", /* host volumes */
            "jenkins" /* switch user */
        );
        assertTrue(slaveTemplate.getCpu() == NomadSlaveTemplate.DEFAULT_CPU);
        assertTrue(slaveTemplate.getMemory() == NomadSlaveTemplate.DEFAULT_MEMORY);
        assertTrue(slaveTemplate.getDisk() == NomadSlaveTemplate.DEFAULT_DISK);
        assertTrue(slaveTemplate.getLabels() == "");
        assertTrue(slaveTemplate.getRemoteFs() == "remoteFs");
        assertTrue(slaveTemplate.getIdleTerminationInMinutes() == NomadSlaveTemplate.DEFAULT_IDLE_TERMINATION_IN_MINUTES);
        assertTrue(slaveTemplate.getReusable() == true);
        assertTrue(slaveTemplate.getNumExecutors() == NomadSlaveTemplate.DEFAULT_NUM_EXECUTORS);
        assertTrue(slaveTemplate.getRegion() == "ams");
        assertTrue(slaveTemplate.getPriority() == NomadSlaveTemplate.DEFAULT_PRIORITY);
        assertTrue(slaveTemplate.getImage() == "image");
        assertTrue(slaveTemplate.getDatacenters() == NomadSlaveTemplate.DEFAULT_DATACENTERS);
        assertTrue(slaveTemplate.getUsername() == "");
        assertTrue(slaveTemplate.getPassword() == "");
        assertTrue(slaveTemplate.getPrivileged() == false);
        assertTrue(slaveTemplate.getNetwork() == "bridge");
        assertTrue(slaveTemplate.getPrefixCmd() == "");
        assertTrue(slaveTemplate.getForcePull() == true);
        assertTrue(slaveTemplate.getHostVolumes() == "/mnt:/mnt");
        assertTrue(slaveTemplate.getSwitchUser() == "jenkins");
    }

    @Test
    public void testNomadSlaveTemplateIsInt() {
        assertTrue(NomadSlaveTemplate.isInt("0"));
        assertTrue(NomadSlaveTemplate.isInt("1"));
        assertTrue(NomadSlaveTemplate.isInt("1234"));
        assertTrue(NomadSlaveTemplate.isInt("300"));

        assertFalse(NomadSlaveTemplate.isInt(""));
        assertFalse(NomadSlaveTemplate.isInt("123abcd"));
        assertFalse(NomadSlaveTemplate.isInt("3.0"));
        assertFalse(NomadSlaveTemplate.isInt("ab123cd"));
        assertFalse(NomadSlaveTemplate.isInt("abcd"));
        assertFalse(NomadSlaveTemplate.isInt("abcd"));
        assertFalse(NomadSlaveTemplate.isInt("abcd123"));
    }

    @Test
    public void testParseDatacenters() {
        assertTrue(NomadSlaveTemplate.parseDatacenters("dc01") == "dc01");
        assertTrue(NomadSlaveTemplate.parseDatacenters("1234") == "1234");

        assertTrue(NomadSlaveTemplate.parseDatacenters("") == NomadSlaveTemplate.DEFAULT_DATACENTERS);
    }

    @Test
    public void testParseCPU() {
        assertTrue(NomadSlaveTemplate.parseCPU("1") == 1);
        assertTrue(NomadSlaveTemplate.parseCPU("300") == 300);

        assertTrue(NomadSlaveTemplate.parseCPU("0") == NomadSlaveTemplate.DEFAULT_CPU);
        assertTrue(NomadSlaveTemplate.parseCPU("-300") == NomadSlaveTemplate.DEFAULT_CPU);
        assertTrue(NomadSlaveTemplate.parseCPU("") == NomadSlaveTemplate.DEFAULT_CPU);
        assertTrue(NomadSlaveTemplate.parseCPU("12.5") == NomadSlaveTemplate.DEFAULT_CPU);
        assertTrue(NomadSlaveTemplate.parseCPU("abc123") == NomadSlaveTemplate.DEFAULT_CPU);
        assertTrue(NomadSlaveTemplate.parseCPU("123abc") == NomadSlaveTemplate.DEFAULT_CPU);
    }

    @Test
    public void testParseDisk() {
        assertTrue(NomadSlaveTemplate.parseDisk("1") == 1);
        assertTrue(NomadSlaveTemplate.parseDisk("500") == 500);

        assertTrue(NomadSlaveTemplate.parseDisk("0") == NomadSlaveTemplate.DEFAULT_DISK);
        assertTrue(NomadSlaveTemplate.parseDisk("-300") == NomadSlaveTemplate.DEFAULT_DISK);
        assertTrue(NomadSlaveTemplate.parseDisk("") == NomadSlaveTemplate.DEFAULT_DISK);
        assertTrue(NomadSlaveTemplate.parseDisk("12.5") == NomadSlaveTemplate.DEFAULT_DISK);
        assertTrue(NomadSlaveTemplate.parseDisk("abc123") == NomadSlaveTemplate.DEFAULT_DISK);
        assertTrue(NomadSlaveTemplate.parseDisk("123abc") == NomadSlaveTemplate.DEFAULT_DISK);
    }

    @Test
    public void testParseIdleTerminationInMinutes() {
        assertTrue(NomadSlaveTemplate.parseIdleTerminationInMinutes("1") == 1);
        assertTrue(NomadSlaveTemplate.parseIdleTerminationInMinutes("500") == 500);
        assertTrue(NomadSlaveTemplate.parseIdleTerminationInMinutes("0") == 0);

        assertTrue(NomadSlaveTemplate.parseIdleTerminationInMinutes("-300") == NomadSlaveTemplate.DEFAULT_IDLE_TERMINATION_IN_MINUTES);
        assertTrue(NomadSlaveTemplate.parseIdleTerminationInMinutes("") == NomadSlaveTemplate.DEFAULT_IDLE_TERMINATION_IN_MINUTES);
        assertTrue(NomadSlaveTemplate.parseIdleTerminationInMinutes("12.5") == NomadSlaveTemplate.DEFAULT_IDLE_TERMINATION_IN_MINUTES);
        assertTrue(NomadSlaveTemplate.parseIdleTerminationInMinutes("abc123") == NomadSlaveTemplate.DEFAULT_IDLE_TERMINATION_IN_MINUTES);
        assertTrue(NomadSlaveTemplate.parseIdleTerminationInMinutes("123abc") == NomadSlaveTemplate.DEFAULT_IDLE_TERMINATION_IN_MINUTES);
    }

    @Test
    public void testParseMemory() {
        assertTrue(NomadSlaveTemplate.parseMemory("1") == 1);
        assertTrue(NomadSlaveTemplate.parseMemory("500") == 500);

        assertTrue(NomadSlaveTemplate.parseMemory("0") == NomadSlaveTemplate.DEFAULT_MEMORY);
        assertTrue(NomadSlaveTemplate.parseMemory("-300") == NomadSlaveTemplate.DEFAULT_MEMORY);
        assertTrue(NomadSlaveTemplate.parseMemory("") == NomadSlaveTemplate.DEFAULT_MEMORY);
        assertTrue(NomadSlaveTemplate.parseMemory("12.5") == NomadSlaveTemplate.DEFAULT_MEMORY);
        assertTrue(NomadSlaveTemplate.parseMemory("abc123") == NomadSlaveTemplate.DEFAULT_MEMORY);
        assertTrue(NomadSlaveTemplate.parseMemory("123abc") == NomadSlaveTemplate.DEFAULT_MEMORY);
    }

    @Test
    public void testParseNumExecutors() {
        assertTrue(NomadSlaveTemplate.parseNumExecutors("1") == 1);
        assertTrue(NomadSlaveTemplate.parseNumExecutors("500") == 500);

        assertTrue(NomadSlaveTemplate.parseNumExecutors("0") == NomadSlaveTemplate.DEFAULT_NUM_EXECUTORS);
        assertTrue(NomadSlaveTemplate.parseNumExecutors("-300") == NomadSlaveTemplate.DEFAULT_NUM_EXECUTORS);
        assertTrue(NomadSlaveTemplate.parseNumExecutors("") == NomadSlaveTemplate.DEFAULT_NUM_EXECUTORS);
        assertTrue(NomadSlaveTemplate.parseNumExecutors("12.5") == NomadSlaveTemplate.DEFAULT_NUM_EXECUTORS);
        assertTrue(NomadSlaveTemplate.parseNumExecutors("abc123") == NomadSlaveTemplate.DEFAULT_NUM_EXECUTORS);
        assertTrue(NomadSlaveTemplate.parseNumExecutors("123abc") == NomadSlaveTemplate.DEFAULT_NUM_EXECUTORS);
    }

    @Test
    public void testParsePriority() {
        assertTrue(NomadSlaveTemplate.parsePriority("1") == 1);
        assertTrue(NomadSlaveTemplate.parsePriority("500") == 500);

        assertTrue(NomadSlaveTemplate.parsePriority("0") == NomadSlaveTemplate.DEFAULT_PRIORITY);
        assertTrue(NomadSlaveTemplate.parsePriority("-300") == NomadSlaveTemplate.DEFAULT_PRIORITY);
        assertTrue(NomadSlaveTemplate.parsePriority("") == NomadSlaveTemplate.DEFAULT_PRIORITY);
        assertTrue(NomadSlaveTemplate.parsePriority("12.5") == NomadSlaveTemplate.DEFAULT_PRIORITY);
        assertTrue(NomadSlaveTemplate.parsePriority("abc123") == NomadSlaveTemplate.DEFAULT_PRIORITY);
        assertTrue(NomadSlaveTemplate.parsePriority("123abc") == NomadSlaveTemplate.DEFAULT_PRIORITY);
    }
}
