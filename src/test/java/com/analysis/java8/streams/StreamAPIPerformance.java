package com.analysis.java8.streams;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.nio.charset.Charset;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;

public class StreamAPIPerformance {
	private List<String> strings = new ArrayList<>();
	
	@Before
	public void generateDataset() {
		Random stringLength = new Random();
		for(int i=0; i<300000; i++) {
			int length = stringLength.nextInt(5);
			byte[] array = new byte[length];
			new Random().nextBytes(array);
			strings.add( new String(array, Charset.forName("UTF-8")) );
		}
	}

	@Test
	public void LoopVsStreamTest1() {

		Instant normalStartTime = Instant.now();
		long emptyCountNormal = 0;
		for(String s : strings) {
			if(s.isEmpty())
				emptyCountNormal++;
		}
		long normalCountTime = Duration.between(normalStartTime, Instant.now()).toMillis();

		Instant streamStartTime = Instant.now();
		long emptyCountStream = strings.stream().filter(s -> s.isEmpty()).count();
		long streamCountTime = Duration.between(streamStartTime, Instant.now()).toMillis();

		System.out.println("LoopVsStreamTest1 ::  Loop Processing Result ["+emptyCountNormal+"]   ||    Stream Processing Time ["+emptyCountStream+"]");
		System.out.println("LoopVsStreamTest1 ::  Loop Processing Time ["+normalCountTime+"]   ||    Stream Processing Time ["+streamCountTime+"]");

		assertThat("Loop Processing Time ["+normalCountTime+"] > Stream Processing Time ["+streamCountTime+"]", is( normalCountTime > streamCountTime));
	}
	
	
	@Test
	public void LoopVsStreamTest2() {

		Instant normalStartTime = Instant.now();
		long emptyCountNormal = 0;
		for(int i=0; i<strings.size(); i++) {
			String s = strings.get(i);
			if(s.isEmpty())
				emptyCountNormal++;
		}
		long normalCountTime = Duration.between(normalStartTime, Instant.now()).toMillis();

		Instant streamStartTime = Instant.now();
		long emptyCountStream = strings.stream().filter(s -> s.isEmpty()).count();
		long streamCountTime = Duration.between(streamStartTime, Instant.now()).toMillis();
		
		System.out.println("LoopVsStreamTest2 ::  Loop Processing Result ["+emptyCountNormal+"]   ||    Stream Processing Time ["+emptyCountStream+"]");
		System.out.println("LoopVsStreamTest2 ::  Loop Processing Time ["+normalCountTime+"]   ||    Stream Processing Time ["+streamCountTime+"]");
		
		assertThat("Loop Processing Time ["+normalCountTime+"] > Stream Processing Time ["+streamCountTime+"]", is( normalCountTime > streamCountTime));
	}

	
	@Test
	public void ForeachVsStreamTest() {
		class EmptyCounter{
			int count = 0; 
		}
		final EmptyCounter emptyCountNormal = new EmptyCounter();
		
		Instant normalStartTime = Instant.now();
		strings.forEach(s ->{
			if(s.isEmpty())
				emptyCountNormal.count++;
		});
		long normalCountTime = Duration.between(normalStartTime, Instant.now()).toMillis();

		Instant streamStartTime = Instant.now();
		long emptyCountStream = strings.stream().filter(s -> s.isEmpty()).count();
		long streamCountTime = Duration.between(streamStartTime, Instant.now()).toMillis();
		
		System.out.println("ForeachVsStreamTest ::  Loop Processing Result ["+emptyCountNormal.count+"]   ||    Stream Processing Time ["+emptyCountStream+"]");
		System.out.println("ForeachVsStreamTest ::  Loop Processing Time ["+normalCountTime+"]   ||    Stream Processing Time ["+streamCountTime+"]");
		
		assertThat("Loop Processing Time ["+normalCountTime+"] > Stream Processing Time ["+streamCountTime+"]", is( normalCountTime > streamCountTime));
	}

	@Test
	public void StreamParallelPerformance() {

		Instant normalStartTime = Instant.now();
		long emptyCountNormal = strings.stream().filter(s -> s.isEmpty()).count();
		long normalCountTime = Duration.between(normalStartTime, Instant.now()).toMillis();
		
		Instant startTime = Instant.now();
		emptyCountNormal = strings.parallelStream().filter(s -> s.isEmpty()).count();
		long parallelCountTime = Duration.between(startTime, Instant.now()).toMillis();
		
		System.out.println("StreamParallelPerformance ::  Normal Stream Processing Time ["+normalCountTime+"]   ||   Parallel Stream Processing Time ["+parallelCountTime+"]");
		assertThat("Normal Stream Processing Time ["+normalCountTime+"] > Parallel Stream Processing Time ["+parallelCountTime+"]", is( normalCountTime > parallelCountTime));
	}
}
