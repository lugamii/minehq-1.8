package dev.lugami.uhub.util;

import dev.lugami.uhub.uHub;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class FileUtil
{
    private File file;
    private FileConfiguration config;
    private String folder;
    private String fileName;
    private boolean isFolder;
    private uHub Core;

    public FileUtil(String folder, String fileName)
    {
        this.file = null;
        this.folder = folder;
        this.fileName = fileName;
        this.isFolder = true;
        if (this.file == null) {
            this.file = new File(Core.getInstance().getDataFolder() + "/" + folder, fileName);
        }
        if (!this.file.exists())
        {
            Core.getInstance().saveResource(folder + "/" + fileName, false);
            this.file = new File(Core.getInstance().getDataFolder() + "/" + folder, fileName);
        }
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public FileUtil(String fileName)
    {
        this.file = null;
        this.isFolder = false;
        this.fileName = fileName;
        if (this.file == null) {
            this.file = new File(Core.getInstance().getDataFolder(), fileName);
        }
        if (!this.file.exists())
        {
            Core.getInstance().saveResource(fileName, false);
            this.file = new File(Core.getInstance().getDataFolder(), fileName);
        }
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public boolean loadFile()
    {
        try
        {
            this.config.load(this.file);
            return true;
        }
        catch (FileNotFoundException e)
        {
            createFile();
            return true;
        }
        catch (IOException e2)
        {
            createFile();
            return false;
        }
        catch (InvalidConfigurationException e3)
        {
            createFile();
        }
        return false;
    }

    public boolean createFile()
    {
        if (this.isFolder)
        {
            if (this.file == null) {
                this.file = new File(Core.getInstance().getDataFolder() + "/" + this.folder, this.fileName);
            }
            if (!this.file.exists())
            {
                Core.getInstance().saveResource(this.folder + "/" + this.fileName, false);
                this.file = new File(Core.getInstance().getDataFolder() + "/" + this.folder, this.fileName);
            }
        }
        else
        {
            if (this.file == null) {
                this.file = new File(Core.getInstance().getDataFolder(), this.fileName);
            }
            if (!this.file.exists())
            {
                Core.getInstance().saveResource(this.fileName, false);
                this.file = new File(Core.getInstance().getDataFolder(), this.fileName);
            }
        }
        this.config = YamlConfiguration.loadConfiguration(this.file);
        return true;
    }

    public boolean exist()
    {
        if (this.file.exists()) {
            return true;
        }
        return false;
    }

    public boolean saveFile()
    {
        try
        {
            this.config.save(this.file);
            return true;
        }
        catch (IOException e)
        {
            System.out.println("Something wrong went saving this file: " + this.file.getName());
        }
        return false;
    }

    public void deleteFile()
    {
        this.file.delete();
    }


    public void set(String path, Object value)
    {
        this.config.set(path, value);
    }

    public int getInt(String path)
    {
        return this.config.getInt(path);
    }

    public String getString(String path)
    {
        return this.config.getString(path);
    }

    public long getLong(String path)
    {
        return this.config.getLong(path);
    }

    public boolean contains(String path)
    {
        return this.config.contains(path);
    }

    public boolean getBoolean(String path)
    {
        return this.config.getBoolean(path);
    }

    public boolean isSet(String path)
    {
        return this.config.isSet(path);
    }

    public ConfigurationSection getConfigurationSection(String path)
    {
        return this.config.getConfigurationSection(path);
    }

    public List<String> getStringList(String path)
    {
        return this.config.getStringList(path);
    }

    public List<Integer> getIntList(String path)
    {
        return this.config.getIntegerList(path);
    }

    public float getFloat(String path)
    {
        return (float)getDouble(path);
    }

    public List<?> getList(String path)
    {
        return this.config.getList(path);
    }

    public double getDouble(String path)
    {
        return this.config.getDouble(path);
    }

    public short getShort(String path)
    {
        return (short)this.config.getInt(path);
    }

    public String getStringColor(String path)
    {
        return ChatColor.translateAlternateColorCodes('&', getString(path));
    }

    public FileConfiguration getConfig()
    {
        return YamlConfiguration.loadConfiguration(this.file);
    }
}