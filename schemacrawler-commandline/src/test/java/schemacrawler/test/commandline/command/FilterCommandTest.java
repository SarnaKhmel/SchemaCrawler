package schemacrawler.test.commandline.command;


import org.junit.jupiter.api.Test;
import picocli.CommandLine;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaCrawlerOptionsBuilder;
import schemacrawler.tools.commandline.command.FilterCommand;
import schemacrawler.tools.commandline.state.SchemaCrawlerShellState;
import schemacrawler.tools.commandline.state.StateFactory;

import static org.apache.commons.lang3.reflect.FieldUtils.readField;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static schemacrawler.test.utility.CommandlineTestUtility.runCommandInTest;
import static schemacrawler.tools.commandline.utility.CommandLineUtility.newCommandLine;

public class FilterCommandTest
{

  @Test
  public void noArgs()
  {
    final String[] args = new String[0];

    final SchemaCrawlerOptionsBuilder builder =
      SchemaCrawlerOptionsBuilder.builder();
    final SchemaCrawlerShellState state = new SchemaCrawlerShellState();
    state.setSchemaCrawlerOptionsBuilder(builder);
    final CommandLine commandLine =
      newCommandLine(FilterCommand.class, new StateFactory(state), true);
    commandLine.parseArgs(args);
    final SchemaCrawlerOptions schemaCrawlerOptions = builder.toOptions();

    assertThat(schemaCrawlerOptions.getParentTableFilterDepth(), is(0));
    assertThat(schemaCrawlerOptions.getChildTableFilterDepth(), is(0));
    assertThat(schemaCrawlerOptions.isNoEmptyTables(), is(false));
  }

  @Test
  public void noValidArgs()
  {
    final String[] args = { "--some-option" };

    final SchemaCrawlerOptionsBuilder builder =
      SchemaCrawlerOptionsBuilder.builder();
    final SchemaCrawlerShellState state = new SchemaCrawlerShellState();
    state.setSchemaCrawlerOptionsBuilder(builder);
    runCommandInTest(new FilterCommand(state), args);
    final SchemaCrawlerOptions schemaCrawlerOptions = builder.toOptions();

    assertThat(schemaCrawlerOptions.getParentTableFilterDepth(), is(0));
    assertThat(schemaCrawlerOptions.getChildTableFilterDepth(), is(0));
    assertThat(schemaCrawlerOptions.isNoEmptyTables(), is(false));
  }

  @Test
  public void parentsBadValue()
  {
    final String[] args = { "--parents", "-1" };

    final SchemaCrawlerOptionsBuilder builder =
      SchemaCrawlerOptionsBuilder.builder();
    final SchemaCrawlerShellState state = new SchemaCrawlerShellState();
    state.setSchemaCrawlerOptionsBuilder(builder);
    assertThrows(CommandLine.ParameterException.class,
                 () -> runCommandInTest(new FilterCommand(state), args));
  }

  @Test
  public void childrenBadValue()
  {
    final String[] args = { "--children", "-1" };

    final SchemaCrawlerOptionsBuilder builder =
      SchemaCrawlerOptionsBuilder.builder();
    final SchemaCrawlerShellState state = new SchemaCrawlerShellState();
    state.setSchemaCrawlerOptionsBuilder(builder);
    assertThrows(CommandLine.ParameterException.class,
                 () -> runCommandInTest(new FilterCommand(state), args));
  }

  @Test
  public void parentsNoValue()
  {
    final String[] args = { "--parents" };

    final SchemaCrawlerOptionsBuilder builder =
      SchemaCrawlerOptionsBuilder.builder();
    final SchemaCrawlerShellState state = new SchemaCrawlerShellState();
    state.setSchemaCrawlerOptionsBuilder(builder);
    assertThrows(CommandLine.ParameterException.class,
                 () -> runCommandInTest(new FilterCommand(state), args));
  }

  @Test
  public void childrenNoValue()
  {
    final String[] args = { "--children" };

    final SchemaCrawlerOptionsBuilder builder =
      SchemaCrawlerOptionsBuilder.builder();
    final SchemaCrawlerShellState state = new SchemaCrawlerShellState();
    state.setSchemaCrawlerOptionsBuilder(builder);
    assertThrows(CommandLine.ParameterException.class,
                 () -> runCommandInTest(new FilterCommand(state), args));
  }

  @Test
  public void allArgs()
    throws IllegalAccessException
  {
    final String[] args = {
      "--parents",
      "2",
      "--children",
      "2",
      "--no-empty-tables=true",
      "additional",
      "-extra"
    };

    final SchemaCrawlerOptionsBuilder builder =
      SchemaCrawlerOptionsBuilder.builder();
    final SchemaCrawlerShellState state = new SchemaCrawlerShellState();
    state.setSchemaCrawlerOptionsBuilder(builder);
    final CommandLine commandLine =
      newCommandLine(FilterCommand.class, new StateFactory(state), true);
    commandLine.execute(args);

    assertThat(readField(builder, "parentTableFilterDepth", true), is(2));
    assertThat(readField(builder, "childTableFilterDepth", true), is(2));
    assertThat(readField(builder, "isNoEmptyTables", true), is(true));

    final SchemaCrawlerOptions schemaCrawlerOptions = builder.toOptions();

    assertThat(schemaCrawlerOptions.getParentTableFilterDepth(), is(2));
    assertThat(schemaCrawlerOptions.getChildTableFilterDepth(), is(2));
    assertThat(schemaCrawlerOptions.isNoEmptyTables(), is(true));
  }

}
