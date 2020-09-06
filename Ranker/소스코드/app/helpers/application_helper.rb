module ApplicationHelper
    
  def asset_present?(name)
    # Rails 4.1 had Rails.application.assets filled out in all environments.
    # Rails 4.2 has it filled only when config.assets.compile == true which is only
    # in development by default.
    if Rails.application.assets.present?
      Rails.application.assets.find_asset(name)
    else
      Rails.application.assets_manifest.files.values.map { |v| v['logical_path'] }.include?(name)
    end
  end
end
