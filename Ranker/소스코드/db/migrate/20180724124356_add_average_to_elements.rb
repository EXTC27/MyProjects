class AddAverageToElements < ActiveRecord::Migration[5.1]
  def change
    add_column :elements, :average, :float
  end
end
