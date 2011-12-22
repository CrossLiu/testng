package org.testng.reporters.jq;

import static org.testng.reporters.jq.Main.C;
import static org.testng.reporters.jq.Main.D;
import static org.testng.reporters.jq.Main.S;

import org.testng.ISuite;
import org.testng.ITestResult;
import org.testng.reporters.XMLStringBuffer;

import java.util.List;

public class SuitePanel extends BasePanel {
  private static final String PASSED = "passed";
  private static final String SKIPPED = "skipped";
  private static final String FAILED = "failed";

  public SuitePanel(Model model) {
    super(model);
  }

  @Override
  public void generate(List<ISuite> suites, XMLStringBuffer xsb) {
    for (ISuite suite : suites) {
      generateSuitePanel(suite, xsb);
    }
  }

  private void generateSuitePanel(ISuite suite, XMLStringBuffer xsb) {
    String divName = getModel().getTag(suite);
    xsb.push(D, C, "panel " + divName);
    {
      ResultsByClass byClass = getModel().getFailedResultsByClass(suite);
      for (Class<?> c : byClass.getClasses()) {
        generateClassPanel(c, byClass.getResults(c), xsb, FAILED);
      }
      byClass = getModel().getSkippedResultsByClass(suite);
      for (Class<?> c : byClass.getClasses()) {
        generateClassPanel(c, byClass.getResults(c), xsb, SKIPPED);
      }
      byClass = getModel().getPassedResultsByClass(suite);
      for (Class<?> c : byClass.getClasses()) {
        generateClassPanel(c, byClass.getResults(c), xsb, PASSED);
      }
    }
    xsb.pop(D);
  }

  private void generateClassPanel(Class c, List<ITestResult> results,XMLStringBuffer xsb,
      String status) {
    xsb.push(D, C, "class-header rounded-window-top");

    // Passed/failed icon
    xsb.addEmptyElement("img", "src", Main.getImage(status));
    xsb.addOptional(S, c.getName(), C, "class-name");
    xsb.pop(D);

    xsb.push(D, C, "class-content rounded-window-bottom");

    for (ITestResult tr : results) {
      generateMethod(tr, xsb);
    }
    xsb.pop(D);
  }

  private void generateMethod(ITestResult tr, XMLStringBuffer xsb) {
    xsb.push(D, C, "method");
    xsb.push(D, C, "method-content");
    xsb.addEmptyElement("a", "name", Model.getTestResultName(tr));
    xsb.addOptional(S, tr.getMethod().getMethodName(), C, "method-name");

    // Parameters?
    if (tr.getParameters().length > 0) {
      StringBuilder sb = new StringBuilder();
      boolean first = true;
      for (Object p : tr.getParameters()) {
        if (!first) sb.append(", ");
        first = false;
        sb.append(p != null ? p.toString() : "<NULL>");
      }
      xsb.addOptional(S, "(" + sb.toString() + ")", C, "parameters");
    }

    // Exception?
    if (tr.getStatus() != ITestResult.SUCCESS && tr.getThrowable() != null) {
      StringBuilder stackTrace = new StringBuilder();
      stackTrace.append("<b>\"")
              .append(tr.getThrowable().getMessage())
              .append("\"</b>")
              .append("<br>");
      for (StackTraceElement str : tr.getThrowable().getStackTrace()) {
        stackTrace.append(str.toString()).append("<br>");
      }
      xsb.addOptional(D, stackTrace.toString() + "\n",
          C, "stack-trace");
    }

//    long time = tr.getEndMillis() - tr.getStartMillis();
//    xsb.addOptional(S, " " + Long.toString(time) + " ms", C, "method-time");
    xsb.pop(D);
    xsb.pop(D);
  }
}
