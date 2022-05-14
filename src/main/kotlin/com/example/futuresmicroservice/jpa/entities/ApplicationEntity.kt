package com.example.futuresmicroservice.jpa.entities

import com.fasterxml.jackson.annotation.JsonManagedReference
import org.jetbrains.kotlin.test.NOARG_COMPILER_PLUGIN_ID
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*


@Entity
@Table(name="application_table")
open class ApplicationEntity {

    constructor(_date : LocalDateTime, _idUser : UserEntity, _count : Long, _price : Double, _idObligation : ObligationEntity): this() {
        this.date = _date
        this.idUser = _idUser
        this.count = _count
        this.price = _price
        this.idObligation = _idObligation
    }

    constructor()

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    open var id: Long? = null

    @Column(name = "date", nullable = false)
    open var date: LocalDateTime? = null

    @OneToOne(cascade = [(CascadeType.MERGE)])
    @JoinColumn(name = "idUser", referencedColumnName = "id")
    open var idUser: UserEntity? = null

    @Column(name = "count", nullable = false)
    open var count: Long? = null

    @Column(name = "price", nullable = false)
    open var price: Double? = null

    @Column(name = "type", nullable = false)
    open var type: String? = null

    @OneToOne(cascade = [(CascadeType.MERGE)])
    @JoinColumn(name = "idObligation", referencedColumnName = "id")
    open var idObligation: ObligationEntity? = null
}