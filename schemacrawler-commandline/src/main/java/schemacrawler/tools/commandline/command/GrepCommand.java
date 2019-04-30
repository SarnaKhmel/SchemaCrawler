/*
========================================================================
SchemaCrawler
http://www.schemacrawler.com
Copyright (c) 2000-2019, Sualeh Fatehi <sualeh@hotmail.com>.
All rights reserved.
------------------------------------------------------------------------

SchemaCrawler is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

SchemaCrawler and the accompanying materials are made available under
the terms of the Eclipse Public License v1.0, GNU General Public License
v3 or GNU Lesser General Public License v3.

You may elect to redistribute this code under any of these licenses.

The Eclipse Public License is available at:
http://www.eclipse.org/legal/epl-v10.html

The GNU General Public License v3 and the GNU Lesser General Public
License v3 are available at:
http://www.gnu.org/licenses/

========================================================================
*/

package schemacrawler.tools.commandline.command;


import static java.util.Objects.requireNonNull;

import java.util.regex.Pattern;

import picocli.CommandLine;
import schemacrawler.schemacrawler.SchemaCrawlerOptionsBuilder;
import schemacrawler.tools.commandline.state.SchemaCrawlerShellState;

/**
 * Parses the command-line.
 *
 * @author Sualeh Fatehi
 */
@CommandLine.Command(name = "grep",
                     header = {
                       "----- Grep Options ------------------------------------------------------------",
                       "Grep for database object metadata",
                     },
                     description = {
                       "",
                     })
public final class GrepCommand
  implements Runnable
{

  private final SchemaCrawlerShellState state;

  @CommandLine.Option(names = {
    "--grep-columns"
  },
                      description = {
                        "<grepcolumns> is a regular expression to match fully qualified column names, "
                        + "in the form \"SCHEMANAME.TABLENAME.COLUMNNAME\" "
                        + "- for example, --grep-columns=.*\\.STREET|.*\\.PRICE "
                        + "matches columns named STREET or PRICE in any table",
                        "Optional, default is no grep"
                      })
  private Pattern grepcolumns;
  @CommandLine.Option(names = {
    "--grep-def"
  },
                      description = {
                        "<grepdef> is a regular expression to match text within remarks and definitions "
                        + "of views, stored proedures and triggers, if available",
                        "Optional, default is no grep"
                      })
  private Pattern grepdef;
  @CommandLine.Option(names = {
    "--grep-in-out"
  },
                      description = {
                        "<grepinout> is a regular expression to match fully qualified inout names, "
                        + "in the form \"SCHEMANAME.ROUTINENAME.INOUTNAME\" "
                        + "- for example, --grep-in-out=.*\\.STREET|.*\\.PRICE "
                        + "matches inouts named STREET or PRICE in any routine",
                        "Optional, default is no grep"
                      })
  private Pattern grepinout;
  @CommandLine.Option(names = {
    "--invert-match"
  },
                      description = {
                        "Inverts the sense of matching, and shows non-matching tables and columns",
                        "Optional, default is false"
                      })
  private Boolean invertMatch;
  @CommandLine.Option(names = {
    "--only-matching"
  },
                      description = {
                        "Shows only matching tables, and does not show foreign keys "
                        + "that reference other non-matching tables",
                        "Optional, default is false"
                      })
  private Boolean onlyMatching;

  public GrepCommand(final SchemaCrawlerShellState state)
  {
    this.state = requireNonNull(state);
  }

  @Override
  public void run()
  {
    final SchemaCrawlerOptionsBuilder optionsBuilder = state.getSchemaCrawlerOptionsBuilder();

    if (grepcolumns != null)
    {
      optionsBuilder.includeGreppedColumns(grepcolumns);
    }
    if (grepinout != null)
    {
      optionsBuilder.includeGreppedRoutineColumns(grepinout);
    }
    if (grepdef != null)
    {
      optionsBuilder.includeGreppedDefinitions(grepdef);
    }

    if (invertMatch != null)
    {
      optionsBuilder.invertGrepMatch(invertMatch);
    }
    if (onlyMatching != null)
    {
      optionsBuilder.grepOnlyMatching(onlyMatching);
    }

  }

}
