/*
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.errorprone;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;

/**
 * Describes a bug pattern detected by error-prone.  Used to generate compiler error messages,
 * for @SuppressWarnings, and to generate the documentation that we host on our web site.
 *
 * @author eaftan@google.com (Eddie Aftandilian)
 */
@Retention(RUNTIME)
public @interface BugPattern {
  /**
   * A unique identifier for this bug, used for @SuppressWarnings and in the compiler error
   * message.
   */
  String name();

  /**
   * Alternate identifiers for this bug, which may also be used in @SuppressWarnings.
   */
  String[] altNames() default {};

  /**
   * The type of link to generate in the compiler error message.
   */
  LinkType linkType() default LinkType.AUTOGENERATED;

  /**
   * The link URL to use if linkType() is LinkType.CUSTOM.
   */
  String link() default "";

  public enum LinkType {
    /**
     * Link to autogenerated documentation, hosted on the error-prone web site.
     */
    AUTOGENERATED,
    /**
     * Custom string.
     */
    CUSTOM,
    /**
     * No link should be displayed.
     */
    NONE
  }

  /**
   * The class of bug this bug checker detects.
   */
  Category category();

  public enum Category {
    /**
     * General Java or JDK errors.
     */
    JDK,
    /**
     * Errors specific to Google Guava.
     */
    GUAVA,
    /**
     * Errors specific to Google Guice.
     */
    GUICE,
    /**
     * Errors specific to Dagger.
     */
    DAGGER,
    /**
     * Errors specific to JUnit.
     */
    JUNIT,
    /**
     * One-off matchers that are not general errors.
     */
    ONE_OFF,
    /**
     *  JSR-330 errors not specific to Guice.
     */
    INJECT,
    /**
     * Errors specific to Mockito.
     */
    MOCKITO,
    /**
     * Errors specific to JMock.
     */
    JMOCK,
    /**
     * Errors specific to Android.
     */
    ANDROID;
  }

  /**
   * A short summary of the problem that this checker detects.  Used for the default compiler error
   * message and for the short description in the generated docs.  Should not end with a period,
   * to match javac warning/error style.
   *
   * <p>Markdown syntax is not allowed for this element.
   */
  String summary();

  /**
   * A longer explanation of the problem that this checker detects.  Used as the main content
   * in the generated documentation for this checker.
   *
   * <p>Markdown syntax is allowed for this element.
   */
  String explanation() default "";

  SeverityLevel severity();

  public enum SeverityLevel {
    ERROR,
    WARNING,
    SUGGESTION,

    /** @deprecated use {@link SUGGESTION} instead */
    @Deprecated
    NOT_A_PROBLEM
  }

  MaturityLevel maturity();

  public enum MaturityLevel {
    MATURE("On by default"),
    EXPERIMENTAL("Experimental");

    final String description;
    MaturityLevel(String description) {
      this.description = description;
    }
  }

  /**
   * Whether this checker should be suppressible, and if so, by what means.
   */
  Suppressibility suppressibility() default Suppressibility.SUPPRESS_WARNINGS;

  public enum Suppressibility {
    /**
     * Can be suppressed using the standard {@code SuppressWarnings("foo")} mechanism. This
     * setting should be used unless there is a good reason otherwise, e.g. security.
     */
    SUPPRESS_WARNINGS(true),
    /**
     * Can be suppressed with a custom annotation on a parent AST node.
     */
    CUSTOM_ANNOTATION(false),
    /**
     * Cannot be suppressed.
     */
    UNSUPPRESSIBLE(false);

    private final boolean disableable;

    Suppressibility(boolean disableable) {
      this.disableable = disableable;
    }

    public boolean disableable() {
      return disableable;
    }
  }

  /**
   * A set of custom suppression annotation types to use if suppressibility is
   * Suppressibility.CUSTOM_ANNOTATION.
   */
  Class<? extends Annotation>[] customSuppressionAnnotations() default {};

  /**
   * Generate an explanation of how to suppress the check.
   *
   * <p>This should only be disabled if the check has a non-standard suppression
   * mechanism that requires additional explanation. For example,
   * {@link SuppressWarnings} cannot be applied to packages, so checks that operate
   * at the package level need special treatment.
   */
  public boolean documentSuppression() default true;

  /**
   * Generate examples from test cases.
   *
   * <p>By default, any positive or negative test inputs  are included in the generated
   * documentation as examples. That behaviour can be disabled if the test inputs aren't good
   * documentation (for example, because they're testing implementation details of the check and
   * aren't representative of real code).
   *
   * <p>If this feature is disabled, make sure to include some representative examples in the
   * explanation.
   */
  public boolean generateExamplesFromTestCases() default true;

}
