package pl.javastart.shortener.link;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.javastart.shortener.link.dto.LinkDto;

public class LinkDtoMapper {

    public static LinkDto map(Link link) {
        String redirectUrl = buildRedirectUrlFromId(link.getId());
        return new LinkDto(link.getId(), link.getName(), link.getTargetUrl(), redirectUrl, link.getVisits());

    }

    private static String buildRedirectUrlFromId(String id){
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/redir/{id}")
                .buildAndExpand(id)
                .toUriString();
    }
}
