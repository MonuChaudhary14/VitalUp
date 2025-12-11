package org.vitalup.vitalup.service.Paging;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vitalup.vitalup.repository.Auth.userRepository;
import org.vitalup.vitalup.service.Interface.IUsernameService;

@Service
@RequiredArgsConstructor
public class UsernameService implements IUsernameService {

	private final userRepository userRepository;

	@Override
	public boolean usernameExists(String username) {
		if (username == null || username.trim().isEmpty()) {
			return false;
		}
		return userRepository.existsByUsername(username.trim());
	}
}

