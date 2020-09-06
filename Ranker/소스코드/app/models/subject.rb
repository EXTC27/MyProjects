class Subject < ApplicationRecord
    validates_uniqueness_of :name
    has_many :details
    has_many :elements
end
