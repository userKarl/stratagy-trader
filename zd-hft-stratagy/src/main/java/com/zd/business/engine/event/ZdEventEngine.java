package com.zd.business.engine.event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

import lombok.Data;

@Data
public class ZdEventEngine<T> {

	private Logger logger;
	
	private Disruptor<T> disruptor;
	
	private ExecutorService executor;
	
	private RingBuffer<T> ringBuffer;
	
	private Map<EventHandler<T>, BatchEventProcessor<T>> handlerProcessorMap;
	
	public ZdEventEngine(Disruptor<T> disruptor,Logger logger) {
		this.disruptor=disruptor;
		this.logger=logger;
	}
	
	public void init() {
		if(logger==null) {
			logger=LoggerFactory.getLogger(ZdEventEngine.class);
		}
		executor= Executors.newCachedThreadPool(DaemonThreadFactory.INSTANCE);
		ringBuffer= disruptor.start();
		handlerProcessorMap = new ConcurrentHashMap<>();
	}
	
	public BatchEventProcessor<T> addHandler(ZdEventDynamicHandler<T> handler) {
    	BatchEventProcessor<T> processor;
    	processor = new BatchEventProcessor<T>(ringBuffer, ringBuffer.newBarrier(), handler);
    	ringBuffer.addGatingSequences(processor.getSequence());
    	executor.execute(processor);
        handlerProcessorMap.put(handler, processor);
    	return processor;
    }
    
    public void removeHandler(ZdEventDynamicHandler<T> handler) {
    	if(handlerProcessorMap.containsKey(handler)) {
        	BatchEventProcessor<T> processor = handlerProcessorMap.get(handler);
            // Remove a processor.
            // Stop the processor
        	processor.halt();
            // Wait for shutdown the complete
        	try {
    			handler.awaitShutdown();
    		} catch (InterruptedException e) {
    			logger.error("关闭handler时发生异常：{}",e);
    		}
            // Remove the gating sequence from the ring buffer
            ringBuffer.removeGatingSequence(processor.getSequence());
            handlerProcessorMap.remove(handler);
    	}else {
    		logger.warn("未找到Processor,无法移除");
    	}

    }
    
}
