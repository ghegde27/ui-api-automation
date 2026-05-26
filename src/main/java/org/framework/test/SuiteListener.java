package org.framework.test;

import org.framework.utils.LogManager;
import org.slf4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;

public final class SuiteListener implements ISuiteListener {

    private static final Logger LOG =
            LogManager.getLogger(SuiteListener.class);

    @Override
    public void onStart(ISuite suite) {
        LOG.info("========== SUITE STARTED : {} ==========", suite.getName());

        ConfigValidator.validate();              // fail fast
        EnvironmentReporter.publish();           // metadata

    }

    @Override
    public void onFinish(ISuite suite) {
        LOG.info("========== SUITE FINISHED : {} ==========", suite.getName());


        ReportArchiver.archiveIfNeeded();
    }
}