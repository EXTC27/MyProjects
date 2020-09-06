class Detail < ApplicationRecord
    mount_uploader :img, PictureUploader
    validates_uniqueness_of :name
    belongs_to :subject
    has_many :elements
end
