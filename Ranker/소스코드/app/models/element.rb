class Element < ApplicationRecord
    before_save :set_average_to_0
    mount_uploader :img, PictureUploader
    validates_uniqueness_of :name
    
    belongs_to :subject
    belongs_to :detail
    has_many :estimations
    
    def set_average_to_0
       self.average = 0 if self.average.nil?
    end
end
