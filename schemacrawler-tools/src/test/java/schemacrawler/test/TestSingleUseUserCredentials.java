/*
========================================================================
SchemaCrawler
http://www.schemacrawler.com
Copyright (c) 2000-2020, Sualeh Fatehi <sualeh@hotmail.com>.
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
package schemacrawler.test;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import schemacrawler.tools.databaseconnector.SingleUseUserCredentials;

public class TestSingleUseUserCredentials
{

  @Test
  public void noCredentials()
  {
    final SingleUseUserCredentials userCredentials =
      new SingleUseUserCredentials();

    assertThat(userCredentials.hasUser(), is(false));
    assertThat(userCredentials.getUser(), is(nullValue()));
    assertThat(userCredentials.hasPassword(), is(false));
    assertThat(userCredentials.getPassword(), is(nullValue()));
  }

  @Test
  public void clearPassword()
  {
    final SingleUseUserCredentials userCredentials =
      new SingleUseUserCredentials("sa", "sa");

    assertThat(userCredentials.hasUser(), is(true));
    assertThat(userCredentials.getUser(), is("sa"));
    assertThat(userCredentials.hasPassword(), is(true));
    assertThat(userCredentials.getPassword(), is("sa"));

    // Get password also clears the password, so assert everything again

    assertThat(userCredentials.hasUser(), is(true));
    assertThat(userCredentials.getUser(), is("sa"));
    assertThat(userCredentials.hasPassword(), is(false));
    assertThrows(IllegalAccessError.class,
                 () -> userCredentials.getPassword());
  }

  @Test
  public void clearPasswordBeforeGetPassword()
  {
    final SingleUseUserCredentials userCredentials =
      new SingleUseUserCredentials("sa", "sa");

    assertThat(userCredentials.hasUser(), is(true));
    assertThat(userCredentials.getUser(), is("sa"));
    assertThat(userCredentials.hasPassword(), is(true));
    // Do not get the password

    userCredentials.clearPassword();

    assertThat(userCredentials.hasUser(), is(true));
    assertThat(userCredentials.getUser(), is("sa"));
    assertThat(userCredentials.hasPassword(), is(false));
    assertThrows(IllegalAccessError.class,
                 () -> userCredentials.getPassword());
  }

}
