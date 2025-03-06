package gr.nlamp.sfgame_backend.player;

import gr.nlamp.sfgame_backend.player.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("player")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    // TODO Use authentication instead of playerId on path
    @GetMapping("increase-skill/{skillType}/{playerId}")
    public ResponseEntity<Void> increaseSkill(@PathVariable("skillType") SkillType skillType,
                                              @PathVariable("playerId") long playerId) {
        playerService.increaseSkill(playerId, skillType);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("basic-info/{playerId}")
    public ResponseEntity<BasicInfoDto> getBasicInfo(@PathVariable("playerId") long playerId) {
        return new ResponseEntity<>(playerService.getBasicInfo(playerId), HttpStatus.OK);
    }

    @GetMapping("profile-main-info/{playerId}")
    public ResponseEntity<ProfileMainInfoDto> getProfileMainInfo(@PathVariable("playerId") long playerId) {
        return new ResponseEntity<>(playerService.getProfileMainInfo(playerId), HttpStatus.OK);
    }

    @PutMapping("description/{playerId}")
    public ResponseEntity<Void> updateDescription(@PathVariable("playerId") long playerId,
                                                  @RequestBody UpdateDescriptionDto dto) {
        playerService.updateDescription(dto, playerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("equipment/{playerId}")
    public ResponseEntity<EquipmentItemDtoList> getEquipment(@PathVariable("playerId") long playerId) {
        return new ResponseEntity<>(playerService.getEquipment(playerId), HttpStatus.OK);
    }

    @GetMapping("bag/{playerId}")
    public ResponseEntity<BagItemDtoList> getBag(@PathVariable("playerId") long playerId) {
        return new ResponseEntity<>(playerService.getBag(playerId), HttpStatus.OK);
    }

    @GetMapping("weapon-shop/{playerId}")
    public ResponseEntity<ShopItemDtoList> getWeaponShop(@PathVariable("playerId") long playerId) {
        return new ResponseEntity<>(playerService.getWeaponShop(playerId), HttpStatus.OK);
    }

    @GetMapping("magic-shop/{playerId}")
    public ResponseEntity<ShopItemDtoList> getMagicShop(@PathVariable("playerId") long playerId) {
        return new ResponseEntity<>(playerService.getMagicShop(playerId), HttpStatus.OK);
    }

    @GetMapping("boosters/{playerId}")
    public ResponseEntity<BoosterDtoList> getBoosters(@PathVariable("playerId") long playerId) {
        return new ResponseEntity<>(playerService.getBoosters(playerId), HttpStatus.OK);
    }

    @GetMapping("weapon-shop/refresh/{playerId}")
    public ResponseEntity<ShopItemDtoList> refreshWeaponShop(@PathVariable("playerId") long playerId) {
        return new ResponseEntity<>(playerService.refreshWeaponShop(playerId), HttpStatus.OK);
    }

    @GetMapping("magic-shop/refresh/{playerId}")
    public ResponseEntity<ShopItemDtoList> refreshMagicShop(@PathVariable("playerId") long playerId) {
        return new ResponseEntity<>(playerService.refreshMagicShop(playerId), HttpStatus.OK);
    }

    @GetMapping("skills-analytical/{playerId}")
    public ResponseEntity<SkillsAnalyticalDto> getSkillsAnalytical(@PathVariable("playerId") long playerId) {
        return new ResponseEntity<>(playerService.getSkillsAnalytical(playerId), HttpStatus.OK);
    }

    @GetMapping("mount/{playerId}")
    public ResponseEntity<MountDto> getMount(@PathVariable("playerId") long playerId) {
        return new ResponseEntity<>(playerService.getMount(playerId), HttpStatus.OK);
    }
}
