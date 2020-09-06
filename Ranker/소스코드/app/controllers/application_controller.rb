class ApplicationController < ActionController::Base
  before_action :db_set
  
  protect_from_forgery with: :exception
  #before_action :authenticate_user!
  
  private
  def db_set
    @subjects = Subject.all
    @details = Detail.all
  end
end
