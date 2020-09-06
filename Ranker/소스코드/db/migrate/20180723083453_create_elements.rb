class CreateElements < ActiveRecord::Migration[5.1]
  def change
    create_table :elements do |t|
      t.belongs_to :subject, foreign_key: true
      t.belongs_to :detail, foreign_key: true
      t.string :name
      t.text   :info
      t.string :img
      
      t.timestamps
    end
  end
end
