package com.example.futuresmicroservice.jpa.entities

import com.fasterxml.jackson.annotation.JsonManagedReference
import org.apache.tomcat.jni.Local
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*


@Entity
@Table(name="obligation_table")
open class ObligationEntity {

    constructor(_date : LocalDateTime, _idSeller : UserEntity, _idBuyer : UserEntity, _count : Long, _price : Double): this() {
        this.date = _date
        this.idSeller = _idSeller
        this.idBuyer = _idBuyer
        this.count = _count
        this.price = _price
    }

    constructor()

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    open var id: Long? = null

    @Column(name = "date", nullable = false)
    open var date: LocalDateTime? = null

    @OneToOne(cascade = [(CascadeType.MERGE)])
    @JoinColumn(name = "idSeller", referencedColumnName = "id")
    open var idSeller: UserEntity? = null

    @OneToOne(cascade = [(CascadeType.MERGE)])
    @JoinColumn(name = "idBuyer", referencedColumnName = "id")
    open var idBuyer: UserEntity? = null

    @Column(name = "count", nullable = false)
    open var count: Long? = null

    @Column(name = "price")
    open var price: Double? = null

}