package com.plog.server.profile.service;

import com.plog.server.profile.domain.Profile;
import com.plog.server.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;

    public Optional<Profile> getProfileByUserUUID(UUID userUUID) {
        return profileRepository.findByUserUserUUID(userUUID);
    }
}
