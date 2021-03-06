package com.hackernight.lwt.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hackernight.lwt.domain.Episode;
import com.hackernight.lwt.repository.EpisodeRepository;
import com.hackernight.lwt.web.rest.errors.BadRequestAlertException;
import com.hackernight.lwt.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Episode.
 */
@RestController
@RequestMapping("/api")
public class EpisodeResource {

    private final Logger log = LoggerFactory.getLogger(EpisodeResource.class);

    private static final String ENTITY_NAME = "episode";

    private final EpisodeRepository episodeRepository;

    public EpisodeResource(EpisodeRepository episodeRepository) {
        this.episodeRepository = episodeRepository;
    }

    /**
     * POST  /episodes : Create a new episode.
     *
     * @param episode the episode to create
     * @return the ResponseEntity with status 201 (Created) and with body the new episode, or with status 400 (Bad Request) if the episode has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/episodes")
    @Timed
    public ResponseEntity<Episode> createEpisode(@Valid @RequestBody Episode episode) throws URISyntaxException {
        log.debug("REST request to save Episode : {}", episode);
        if (episode.getId() != null) {
            throw new BadRequestAlertException("A new episode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Episode result = episodeRepository.save(episode);
        return ResponseEntity.created(new URI("/api/episodes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /episodes : Updates an existing episode.
     *
     * @param episode the episode to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated episode,
     * or with status 400 (Bad Request) if the episode is not valid,
     * or with status 500 (Internal Server Error) if the episode couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/episodes")
    @Timed
    public ResponseEntity<Episode> updateEpisode(@Valid @RequestBody Episode episode) throws URISyntaxException {
        log.debug("REST request to update Episode : {}", episode);
        if (episode.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Episode result = episodeRepository.save(episode);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, episode.getId().toString()))
            .body(result);
    }

    /**
     * GET  /episodes : get all the episodes.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of episodes in body
     */
    @GetMapping("/episodes")
    @Timed
    public List<Episode> getAllEpisodes(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Episodes");
        return episodeRepository.findAllWithEagerRelationships();
    }

    /**
     * GET  /episodes/:id : get the "id" episode.
     *
     * @param id the id of the episode to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the episode, or with status 404 (Not Found)
     */
    @GetMapping("/episodes/{id}")
    @Timed
    public ResponseEntity<Episode> getEpisode(@PathVariable Long id) {
        log.debug("REST request to get Episode : {}", id);
        Optional<Episode> episode = episodeRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(episode);
    }

    /**
     * DELETE  /episodes/:id : delete the "id" episode.
     *
     * @param id the id of the episode to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/episodes/{id}")
    @Timed
    public ResponseEntity<Void> deleteEpisode(@PathVariable Long id) {
        log.debug("REST request to delete Episode : {}", id);

        episodeRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
