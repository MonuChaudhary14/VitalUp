package org.vitalup.vitalup.service.Interface;

public interface IRateLimitService {

	boolean tryConsume(String key);

}

