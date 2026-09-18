package com.datajpa.demo.jpa_mappings;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/accounts")
public class UserAccountController {

    private final UserAccountRepository userAccountRepository; //Has-A dependency
     private final AddressRepository addressRepository;
    private final PostRepository postRepository;

    @GetMapping
    public String info(){
       return "Greetings";
    }

    @Autowired
    public UserAccountController(UserAccountRepository userAccountRepository, AddressRepository addressRepository, PostRepository postRepository) {
        this.userAccountRepository = userAccountRepository;
        this.addressRepository = addressRepository;
        this.postRepository = postRepository;
    }

    @PostMapping
    public UserAccount registerUser(@RequestBody UserAccount userAccount){
             return  this.userAccountRepository.save(userAccount);
    }

    @PostMapping("/{id}/address")
   // @Transactional
    public  UserAccount registerUserAddress(@RequestBody Address newAddress, @PathVariable("id") Integer userId){
        UserAccount foundUserAccount = this.userAccountRepository
                .findById(userId).orElseThrow(() -> new RuntimeException("User id not exist"));
        Address address = this.addressRepository.save(newAddress); // persit address
        foundUserAccount.setAddress(address); // assign address to User
        return  this.userAccountRepository.save(foundUserAccount);

    }

    @PostMapping("{id}/posts")
    public  UserAccount addUserPosts(@RequestBody Post newPost,@PathVariable Integer id){
        UserAccount foundUserAccount = this.userAccountRepository
                .findById(id).orElseThrow(() -> new RuntimeException("User id not exist"));
        Post savedPost = this.postRepository.save(newPost);
        foundUserAccount.getPosts().add(savedPost);
        return  this.userAccountRepository.save(foundUserAccount);
    }

    @PostMapping("{id}")
    public  UserAccount getUserPosts(@PathVariable Integer id){
        return this.userAccountRepository
                .findById(id).orElseThrow(() -> new RuntimeException("User id not exist"));

    }

}
