GoldenMaster
=============
[![Build Status](https://api.travis-ci.org/almondtools/goldenmaster.svg)](https://travis-ci.org/almondtools/goldenmaster)
[![codecov](https://codecov.io/gh/almondtools/goldenmaster/branch/master/graph/badge.svg)](https://codecov.io/gh/almondtools/goldenmaster)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/c5cf275a6ff9468f95b22b8998b52e1d)](https://www.codacy.com/project/almondtools/goldenmaster/dashboard?utm_source=github.com&utm_medium=referral&utm_content=almondtools/goldenmaster&utm_campaign=Badge_Grade_Dashboard)

An extension to manage golden master tests in JUnit 5. Heavily inspired by from [golden-master](https://github.com/maxbechtold/golden-master).

Usage
=====
To enable GoldenmasterTesting annotate the class or method containing your golden master tests with `@GoldenMasterTest`. This loads the `GoldenMasterExtension` which manages the lifecycle of the golden master tests:

* load/deserialize the expected data from the files <testname>.expected
* provide an Object `Expectations` as resolvable parameter in methods
* request approval/rejection after all tests of the run are executed

`GoldenMasterExtension` does only effect methods with a parameter of type `Expectations`, other methods behave like ordinary JUnit test methods.

Example
=======
```
	@GoldenMasterTest(store="src/test/resources")
	@Test
	void testA(Expectations expectations) {
		Example next = new Example("astring", true, 1, -1).next();
		expectations.validate(next.getA() + ":" + next.isB() + ":" + next.getC() + ":" + next.getD());
	}
```

The full example is found [here](/src/test/java/net/amygdalum/goldenmaster/example/). The serialized expectations are found [here](/src/test/resources/).


Maven Dependency
================

```xml
<dependency>
    <groupId>net.amygdalum</groupId>
    <artifactId>goldenmaster</artifactId>
    <version>0.0.3</version>
</dependency>
```