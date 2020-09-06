class User < ApplicationRecord
  has_many :elements
  has_many :estimations
  has_many :requests
  # Include default devise modules. Others available are:
  # :confirmable, :lockable, :timeoutable and :omniauthable
  devise :database_authenticatable, :registerable,
         :recoverable, :rememberable, :trackable, :validatable
         
  def get_id
    self.email.split('@').first
  end
end
