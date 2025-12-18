package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.ProfileDao;
import org.yearup.models.Profile;

@RestController
@RequestMapping("/profile")
@CrossOrigin
public class ProfileController
{
    private ProfileDao profileDao;

    @Autowired
    public ProfileController(ProfileDao profileDao)
    {
        this.profileDao = profileDao;
    }

    @PostMapping
    public Profile create(@RequestBody Profile profile)
    {
        return profileDao.create(profile);
    }

    @GetMapping("{id}")
    public Profile getById(@PathVariable int id)
    {
        //get profile by id
        return profileDao.getById(id);
    }

    @PutMapping("{id}")
    public void update(@PathVariable int id, @RequestBody Profile profile)
    {
        //update profile
        profileDao.update(id, profile);
    }
}
