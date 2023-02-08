package com.example.starter.routes.user

enum class UserView {
  DEFAULT {
    override fun getView(): UserDtoView = UserDtoView.DEFAULT()
  },
  STATUS {
    override fun getView(): UserDtoView = UserDtoView.STATUS()
  },
  GRANT {
    override fun getView(): UserDtoView = UserDtoView.GRANT()
  },
  MIGRATION {
    override fun getView(): UserDtoView = UserDtoView.MIGRATION()
  },
  KYC {
    override fun getView(): UserDtoView = UserDtoView.KYC()
  };

  abstract fun getView(): UserDtoView
}
