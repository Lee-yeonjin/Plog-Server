package com.plog.server.profile.api;

import com.plog.server.profile.domain.Profile;
import com.plog.server.profile.service.ProfileService;
import com.plog.server.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profiler")
@Slf4j
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/active")
    public List<Profile> getActiveUsers() {
        return profileService.getActivePlogging();
    }
}
