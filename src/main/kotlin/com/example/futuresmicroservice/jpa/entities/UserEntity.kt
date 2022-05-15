package com.example.futuresmicroservice.jpa.entities

import com.fasterxml.jackson.annotation.JsonManagedReference
import javax.persistence.*

@Entity
@Table(name="user_table")
open class UserEntity {

    constructor(_login : String, _password : String, _role : String, _name : String): this() {
        this.login = _login
        this.password = _password
        this.role = _role
        this.name = _name
    }

    constructor()

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    open var id: Long? = null

    @Column(name = "login", nullable = false)
    open var login: String? = null

    @Column(name = "password", nullable = false)
    open var password: String? = null

    @Column(name = "role", nullable = false)
    open var role: String? = null

    @Column(name = "name", nullable = false)
    open var name: String? = null

    @Column(name = "salt")
    open var salt: String? = null

//    @OneToMany(mappedBy = "idSeller", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
//    @JoinColumn(name = "idBuyer", referencedColumnName = "id")
//    private val applicationList: List<ObligationEntity>? = null
//
//    @OneToMany(mappedBy = "idSeller", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
//    @JsonManagedReference
//    private val idSellerList: List<ObligationEntity>? = null
}