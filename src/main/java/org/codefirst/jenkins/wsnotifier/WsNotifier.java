package org.codefirst.jenkins.wsnotifier;
import hudson.Launcher;
import hudson.Extension;
import hudson.util.FormValidation;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.AbstractProject;
import hudson.tasks.*;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;
import java.io.IOException;

public class WsNotifier extends Notifier {
    @DataBoundConstructor
    public WsNotifier() {
    }

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) {
        WsServer.send(build);
        return true;
    }

    public BuildStepMonitor getRequiredMonitorService(){
        return BuildStepMonitor.NONE;
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }

    @Extension // This indicates to Jenkins that this is an implementation of an extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {
        private int port = 8081;

        public int port(){ return port; }
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project types
            return true;
        }

        public String getDisplayName() {
            return "Websocket Notifier";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            port = formData.getInt("port");
            save();
            return super.configure(req,formData);
        }
    }
}
