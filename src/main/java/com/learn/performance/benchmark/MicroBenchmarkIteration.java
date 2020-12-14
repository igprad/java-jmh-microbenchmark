package com.learn.performance.benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
public class MicroBenchmarkIteration {

  private List<String> names;

  @Setup
  public void setUp() {
    names = new ArrayList<>();
    IntStream.range(1, 10000000).forEach(i -> names.add(String.valueOf(i)));
  }

  @Benchmark
  public void test_for(Blackhole blackhole) {
    for (int i = 0; i < names.size(); i++) {
      blackhole.consume(names.get(i));
    }
  }

  @Benchmark
  public void test_while(Blackhole blackhole) {
    int i = 0;
    while (i < names.size()) {
      blackhole.consume(names.get(i));
      i++;
    }
  }

  @Benchmark
  public void test_forEach(Blackhole blackhole) {
    for (String name : names) {
      blackhole.consume(name);
    }
  }

  @Benchmark
  public void test_iterator(Blackhole blackhole) {
    Iterator<String> iterator = names.iterator();
    while (iterator.hasNext()) {
      blackhole.consume(iterator.next());
    }
  }

  @Benchmark
  public void test_stream_forEach(Blackhole blackhole) {
    names.forEach(name -> blackhole.consume(names));
  }

  public static void main(String[] args) throws RunnerException {
    new Runner(
            new OptionsBuilder()
                .include(MicroBenchmarkIteration.class.getSimpleName())
                .forks(1)
                .build())
        .run();
  }
}
