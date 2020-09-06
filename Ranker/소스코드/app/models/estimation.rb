class Estimation < ApplicationRecord
    validates_uniqueness_of :user_id, :scope => :element_id
    belongs_to :user
    belongs_to :element
end
