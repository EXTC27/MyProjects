class CreateEstimations < ActiveRecord::Migration[5.1]
  def change
    create_table :estimations do |t|
      t.belongs_to :user, foreign_key: true
      t.belongs_to :element, foreign_key: true
      t.integer :saticification
      t.string :comment
      
      t.timestamps
    end
  end
end
