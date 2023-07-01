GoldenMaster
=============
[![codecov](https://codecov.io/gh/almondtools/goldenmaster/branch/master/graph/badge.svg)](https://codecov.io/gh/almondtools/goldenmaster)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/bc60e8354d274f3793f53c803fb6b501)](https://app.codacy.com/gh/almondtools/goldenmaster/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)

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
