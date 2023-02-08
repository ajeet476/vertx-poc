package com.example.starter.routes.user

import com.fasterxml.jackson.annotation.JsonView
import java.util.UUID

data class UserDto(
  @field:JsonView(UserDtoView.DEFAULT::class)
  val id: UUID,

  @field:JsonView(UserDtoView.DEFAULT::class)
  val memberId: Long = 0L,

  @field:JsonView(UserDtoView.GRANT::class)
  val grantStatus: Boolean,

  @field:JsonView(UserDtoView.STATUS::class)
  val status: Boolean,

  @field:JsonView(UserDtoView.KYC::class)
  val kycStatus: Boolean,

  @field:JsonView(UserDtoView.MIGRATION::class)
  val migrationStatus: Boolean,

  @field:JsonView(UserDtoView.DEFAULT::class)
  val note: String
)

interface UserDtoView {
  class DEFAULT: UserDtoView
  class STATUS: UserDtoView
  class GRANT: UserDtoView
  class MIGRATION: UserDtoView
  class KYC: UserDtoView
}
