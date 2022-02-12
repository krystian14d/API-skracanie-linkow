package pl.javastart.shortener.link;

import liquibase.pro.packaged.P;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.javastart.shortener.link.dto.*;

import java.net.URI;

@RestController
@RequestMapping("/api/links")
public class LinkResource {
    private final LinkService linkService;

    public LinkResource(LinkService linkService) {
        this.linkService = linkService;
    }

    @PostMapping
    ResponseEntity<LinkDto> save(@RequestBody LinkCreateDto link){
        LinkDto linkDto = linkService.shortenLink(link);
        URI savedEntityLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(linkDto.getId())
                .toUri();
        return ResponseEntity.created(savedEntityLocation).body(linkDto);
    }

    @GetMapping("/{id}")
    ResponseEntity<LinkDto> findById (@PathVariable String id){
        return linkService.findLinkById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    ResponseEntity<?> update(@PathVariable String id,
                             @RequestBody LinkUpdateDto link){
        try{
            linkService.updateLink(id, link);
            return ResponseEntity.noContent().build();
        }catch(LinkNotFoundException e){
            return ResponseEntity.notFound().build();
        }catch (InvalidPasswordException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .header("reason", e.getMessage())
                    .build();
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteById(@PathVariable String id, @RequestHeader String passwd){
        try{
            linkService.deleteById(id, passwd);
            return ResponseEntity.noContent().build();
        }catch (InvalidPasswordException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .header("reason", e.getMessage())
                    .build();
        }
    }
}
