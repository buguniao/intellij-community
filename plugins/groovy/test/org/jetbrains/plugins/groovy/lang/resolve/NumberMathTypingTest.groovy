/*
 * Copyright 2000-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jetbrains.plugins.groovy.lang.resolve

import com.intellij.psi.PsiClassType
import com.intellij.testFramework.LightProjectDescriptor
import org.jetbrains.plugins.groovy.GroovyLightProjectDescriptor
import org.jetbrains.plugins.groovy.LightGroovyTestCase
import org.jetbrains.plugins.groovy.lang.psi.GroovyFile
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrExpression

class NumberMathTypingTest extends LightGroovyTestCase {

  final LightProjectDescriptor projectDescriptor = GroovyLightProjectDescriptor.GROOVY_LATEST

  @Override
  void setUp() throws Exception {
    super.setUp();
    addBigInteger()
    addBigDecimal()
  }

  void 'test +'() {
    def data = [
      ['Byte', 'Byte', 'Integer'],
      ['Byte', 'Character', 'Integer'],
      ['Byte', 'Short', 'Integer'],
      ['Byte', 'Integer', 'Integer'],
      ['Byte', 'Long', 'Long'],
      ['Byte', 'BigInteger', 'BigInteger'],
      ['Byte', 'BigDecimal', 'BigDecimal'],
      ['Byte', 'Float', 'Double'],
      ['Byte', 'Double', 'Double'],

      ['Character', 'Byte', 'Integer'],
      ['Character', 'Character', 'Integer'],
      ['Character', 'Short', 'Integer'],
      ['Character', 'Integer', 'Integer'],
      ['Character', 'Long', 'Long'],
      ['Character', 'BigInteger', 'BigInteger'],
      ['Character', 'BigDecimal', 'BigDecimal'],
      ['Character', 'Float', 'Double'],
      ['Character', 'Double', 'Double'],

      ['Short', 'Byte', 'Integer'],
      ['Short', 'Character', 'Integer'],
      ['Short', 'Short', 'Integer'],
      ['Short', 'Integer', 'Integer'],
      ['Short', 'Long', 'Long'],
      ['Short', 'BigInteger', 'BigInteger'],
      ['Short', 'BigDecimal', 'BigDecimal'],
      ['Short', 'Float', 'Double'],
      ['Short', 'Double', 'Double'],

      ['Integer', 'Byte', 'Integer'],
      ['Integer', 'Character', 'Integer'],
      ['Integer', 'Short', 'Integer'],
      ['Integer', 'Integer', 'Integer'],
      ['Integer', 'Long', 'Long'],
      ['Integer', 'BigInteger', 'BigInteger'],
      ['Integer', 'BigDecimal', 'BigDecimal'],
      ['Integer', 'Float', 'Double'],
      ['Integer', 'Double', 'Double'],

      ['Long', 'Byte', 'Long'],
      ['Long', 'Character', 'Long'],
      ['Long', 'Short', 'Long'],
      ['Long', 'Integer', 'Long'],
      ['Long', 'Long', 'Long'],
      ['Long', 'BigInteger', 'BigInteger'],
      ['Long', 'BigDecimal', 'BigDecimal'],
      ['Long', 'Float', 'Double'],
      ['Long', 'Double', 'Double'],

      ['BigInteger', 'Byte', 'BigInteger'],
      ['BigInteger', 'Character', 'BigInteger'],
      ['BigInteger', 'Short', 'BigInteger'],
      ['BigInteger', 'Integer', 'BigInteger'],
      ['BigInteger', 'Long', 'BigInteger'],
      ['BigInteger', 'BigInteger', 'BigInteger'],
      ['BigInteger', 'BigDecimal', 'BigDecimal'],
      ['BigInteger', 'Float', 'Double'],
      ['BigInteger', 'Double', 'Double'],

      ['BigDecimal', 'Byte', 'BigDecimal'],
      ['BigDecimal', 'Character', 'BigDecimal'],
      ['BigDecimal', 'Short', 'BigDecimal'],
      ['BigDecimal', 'Integer', 'BigDecimal'],
      ['BigDecimal', 'Long', 'BigDecimal'],
      ['BigDecimal', 'BigInteger', 'BigDecimal'],
      ['BigDecimal', 'BigDecimal', 'BigDecimal'],
      ['BigDecimal', 'Float', 'Double'],
      ['BigDecimal', 'Double', 'Double'],

      ['Float', 'Byte', 'Double'],
      ['Float', 'Character', 'Double'],
      ['Float', 'Short', 'Double'],
      ['Float', 'Integer', 'Double'],
      ['Float', 'Long', 'Double'],
      ['Float', 'BigInteger', 'Double'],
      ['Float', 'BigDecimal', 'Double'],
      ['Float', 'Float', 'Double'],
      ['Float', 'Double', 'Double'],

      ['Double', 'Byte', 'Double'],
      ['Double', 'Character', 'Double'],
      ['Double', 'Short', 'Double'],
      ['Double', 'Integer', 'Double'],
      ['Double', 'Long', 'Double'],
      ['Double', 'BigInteger', 'Double'],
      ['Double', 'BigDecimal', 'Double'],
      ['Double', 'Float', 'Double'],
      ['Double', 'Double', 'Double'],
    ]
    for (row in data) {
      def (left, right, type) = row
      doTest left, '+', right, type
    }
  }

  private void doTest(String left, String operator, String right, String expected) {
    fixture.with {
      def file = configureByText('_.groovy', """\
def foo($left a, $right b) {
  a $operator b
}
""") as GroovyFile
      try {
        def expr = file.methods.first().block.statements.first()
        assert expr instanceof GrExpression
        def type = expr.type
        assert type instanceof PsiClassType
        def name = type.className
        assert name == expected
      }
      catch (Throwable e) {
        throw new RuntimeException("$left $operator $right (expected: $expected)", e)
      }
    }
  }
}
