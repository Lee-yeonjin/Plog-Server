package com.plog.server.trash.api;

import com.plog.server.global.ApiResponse;
import com.plog.server.trash.domain.Trash;
import com.plog.server.trash.dto.TrashDto;
import com.plog.server.trash.service.TrashService;
import com.plog.server.user.domain.User;
import com.plog.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/trash")
@RequiredArgsConstructor
public class TrashController {
    private final TrashService trashService;
    private final UserService userService;

    @PostMapping("/{uuid}/record")
    public ResponseEntity<ApiResponse> createTrash(@PathVariable UUID uuid, @RequestBody TrashDto trashDto) {
        User user = userService.getUserByUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Trash trash = Trash.builder()
                .garbage(trashDto.getGarbage())
                .can(trashDto.getCan())
                .plastic(trashDto.getPlastic())
                .paper(trashDto.getPaper())
                .plastic_bag(trashDto.getPlastic_bag())
                .glass(trashDto.getGlass())
                .build();

        trashService.createTrash(trash);
        return ResponseEntity.ok(new ApiResponse(("플로깅 기록 저장 성공")));
    }

    @GetMapping("/{uuid}/plogging-check")
    public ResponseEntity<List<Trash>> getAllTrash(@PathVariable UUID uuid) {
        User user = userService.getUserByUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Trash> trashList = trashService.getAllTrash();
        return ResponseEntity.ok(trashList);
    }
}