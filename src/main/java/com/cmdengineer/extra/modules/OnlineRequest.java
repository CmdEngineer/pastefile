package com.cmdengineer.extra.modules;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.mrcrayfish.device.util.StreamUtils;

public class OnlineRequest {
	private static OnlineRequest instance = null;

	private final Queue<RequestWrapper> requests;

	private Thread thread;
	private boolean running = true;
	
	private OnlineRequest() 
	{
		this.requests = new ConcurrentLinkedQueue<>();
		start();
	}
	public static OnlineRequest getInstance() 
	{
		if(instance == null) 
		{
			instance = new OnlineRequest();
		}
		return instance;
	}
	
	private void start() 
	{
		thread = new Thread(new RequestRunnable(), "Online Request Thread");
		thread.start();
	}
	
	public void make(String url, ResponseHandler handler)
	{
		synchronized(requests)
		{
			requests.offer(new RequestWrapper(url, handler));
			requests.notify();
		}
	}
	
	private class RequestRunnable implements Runnable 
	{
		@Override
		public void run()
		{
			while(running) 
			{
				try
				{
					synchronized(requests)
					{
						requests.wait();
					}
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}

				while(!requests.isEmpty())
				{
					RequestWrapper wrapper = requests.poll();
					try(CloseableHttpClient client = HttpClients.createDefault())
					{
						HttpGet get = new HttpGet(wrapper.url);
						try(CloseableHttpResponse response = client.execute(get))
						{
							String raw = StreamUtils.convertToString(response.getEntity().getContent());
							wrapper.handler.handle(true, raw);
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
						wrapper.handler.handle(false, "");
					}
				}
			}
		}
	}
	
	private static class RequestWrapper 
	{
		public final String url;
		public final ResponseHandler handler;
		
		public RequestWrapper(String url, ResponseHandler handler)
		{
			this.url = url;
			this.handler = handler;
		}
	}
	
	public interface ResponseHandler
	{
		void handle(boolean success, String response);
	}
}
