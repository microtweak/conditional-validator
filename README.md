# Conditional Validator

[![][maven img]][maven]
[![][javadoc img]][javadoc]
[![][release img]][release]
[![][license img]][license]

[maven]:http://search.maven.org/#search|gav|1|g:"com.github.microtweak"%20AND%20a:"conditional-validator-core"
[maven img]:https://maven-badges.herokuapp.com/maven-central/com.github.microtweak/conditional-validator-core/badge.svg

[javadoc]:https://javadoc.io/doc/com.github.microtweak/conditional-validator-core
[javadoc img]:https://javadoc.io/badge/com.github.microtweak/conditional-validator-core.svg

[release]:https://github.com/microtweak/conditional-validator/releases
[release img]:https://img.shields.io/github/release/microtweak/conditional-validator.svg

[license]:LICENSE
[license img]:https://img.shields.io/badge/License-MIT-yellow.svg

## Problem
The constraints of the [Bean Validation](https://beanvalidation.org) cannot be turned on/off programmatically or according to any condition of the model object.

The [@GroupSequenceProvider](https://docs.jboss.org/hibernate/stable/validator/api/) annotation from [Hibernate Validator (RI)](http://hibernate.org/validator/) allows you to emulate this feature, however, it is a bit boring and tiring implement a class for each validated model object.

## Solution
An extension for Bean Validation 2.0 containing analogous annotations for each constraint. For example, if you want to apply @NotNull conditionally use @NotNullWhen(expression = "<condition>")

Currently, the constraint expression is provided by [Commons Jexl](http://commons.apache.org/proper/commons-jexl/). All provided expression must return a Boolean (true/false).

Whenever the expression returns true, Conditional Validator delegates to the provider (Hibernate Validator or Apache BVal) the corresponding validation. For example, when the @NotNulWhen expression is true, ConditionalValidator tells the provider to validate as @NotNull.

## Usage

1. Add dependency to pom.xml

```xml
<!-- If you use Hibernate Validator (RI) -->
<dependency>
    <groupId>com.github.microtweak</groupId>
    <artifactId>conditional-validator-hv</artifactId>
    <version>${conditional-validator.version}</version>
</dependency>

<!-- If you use Apache BVal -->
<dependency>
    <groupId>com.github.microtweak</groupId>
    <artifactId>conditional-validator-bval</artifactId>
    <version>${conditional-validator.version}</version>
</dependency>
```

2. Add annotations Conditional Validator

```java
@ConditionalValidate // Enable conditional validation on this class
public class User {

    private boolean notifyByEmail;

    @EmailWhen(expression = "notifyByEmail") // Add the conditional constraint and set the expression
    private String email;
    
    // Getters and Setters
}
```