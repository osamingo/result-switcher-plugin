package org.jenkinsci.plugins;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import twitter4j.TwitterException;

public class ResultSwitcherNotifier extends Notifier {

	private static final String DISPLAY_NAME = "Result Switcher";

	private final String success;

	private final String notSuccess;

	@DataBoundConstructor
	public ResultSwitcherNotifier(String success, String notSuccess) {
		this.success = success;
		this.notSuccess = notSuccess;
	}

	public String getSuccess() {
		return success;
	}

	public String getNotSuccess() {
		return notSuccess;
	}

	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {

		String message = null;

		listener.getLogger().println("Result Swither -> START");

		if (build != null && build.getResult() == Result.SUCCESS) {
			// 成功時
			message = success;

		} else {
			// 失敗時
			message = notSuccess;
		}

		try {
			new ResultAction().tweet(message);
			listener.getLogger().println("Tweet Message -> " + message);
		} catch (TwitterException e) {
			listener.getLogger().println(e.getMessage());
		} finally {
			listener.getLogger().println("Result Swither -> END");
		}

		return true;
	}

	@Override
	public BuildStepDescriptor getDescriptor() {
		return (DescriptorImpl) super.getDescriptor();
	}

	@Extension
	public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {

		@Override
		public boolean isApplicable(@SuppressWarnings("rawtypes") Class<? extends AbstractProject> jobType) {
			return true;
		}

		@Override
		public String getDisplayName() {
			return DISPLAY_NAME;
		}

		@Override
		public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
			save();
			return super.configure(req, json);
		}

	}

	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;
	}

}
